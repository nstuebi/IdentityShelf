import { useState, useEffect } from 'react'
import { 
  IdentifierType, 
  IdentityTypeIdentifierMapping, 
  listIdentifierTypes,
  getIdentityTypeIdentifierMappings,
  createIdentityTypeIdentifierMapping,
  updateIdentityTypeIdentifierMapping,
  deleteIdentityTypeIdentifierMapping
} from '../api/client'

interface IdentifierMappingManagerProps {
  identityTypeId: string
  identityTypeName: string
  onMappingsChange?: (mappings: IdentityTypeIdentifierMapping[]) => void
}

export default function IdentifierMappingManager({ 
  identityTypeId, 
  identityTypeName, 
  onMappingsChange 
}: IdentifierMappingManagerProps) {
  const [mappings, setMappings] = useState<IdentityTypeIdentifierMapping[]>([])
  const [availableIdentifiers, setAvailableIdentifiers] = useState<IdentifierType[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showAddForm, setShowAddForm] = useState(false)
  const [editingMapping, setEditingMapping] = useState<IdentityTypeIdentifierMapping | null>(null)
  
  // Form state
  const [selectedIdentifierType, setSelectedIdentifierType] = useState('')
  const [sortOrder, setSortOrder] = useState(1)
  const [required, setRequired] = useState(false)
  const [primaryCandidate, setPrimaryCandidate] = useState(false)
  const [overrideValidationRegex, setOverrideValidationRegex] = useState('')
  const [overrideDefaultValue, setOverrideDefaultValue] = useState('')

  useEffect(() => {
    loadData()
  }, [identityTypeId])

  const loadData = async () => {
    try {
      setLoading(true)
      const [mappingsData, identifierTypesData] = await Promise.all([
        getIdentityTypeIdentifierMappings(identityTypeId),
        listIdentifierTypes()
      ])
      
      setMappings(mappingsData)
      setAvailableIdentifiers(identifierTypesData.filter(it => it.active))
      setError(null)
      
      if (onMappingsChange) {
        onMappingsChange(mappingsData)
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load data')
    } finally {
      setLoading(false)
    }
  }

  const resetForm = () => {
    setSelectedIdentifierType('')
    setSortOrder(mappings.length + 1)
    setRequired(false)
    setPrimaryCandidate(false)
    setOverrideValidationRegex('')
    setOverrideDefaultValue('')
    setEditingMapping(null)
  }

  const handleAdd = () => {
    resetForm()
    setShowAddForm(true)
  }

  const handleEdit = (mapping: IdentityTypeIdentifierMapping) => {
    setSelectedIdentifierType(mapping.identifierTypeId)
    setSortOrder(mapping.sortOrder)
    setRequired(mapping.required)
    setPrimaryCandidate(mapping.primaryCandidate)
    setOverrideValidationRegex(mapping.overrideValidationRegex || '')
    setOverrideDefaultValue(mapping.overrideDefaultValue || '')
    setEditingMapping(mapping)
    setShowAddForm(true)
  }

  const handleSave = async () => {
    if (!selectedIdentifierType) {
      setError('Please select an identifier type')
      return
    }

    try {
      const data = {
        identityTypeId,
        identifierTypeId: selectedIdentifierType,
        sortOrder,
        required,
        primaryCandidate,
        overrideValidationRegex: overrideValidationRegex.trim() || undefined,
        overrideDefaultValue: overrideDefaultValue.trim() || undefined
      }

      if (editingMapping) {
        await updateIdentityTypeIdentifierMapping(editingMapping.id, data)
      } else {
        await createIdentityTypeIdentifierMapping(data)
      }

      await loadData()
      setShowAddForm(false)
      resetForm()
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to save mapping')
    }
  }

  const handleDelete = async (mappingId: string, identifierName: string) => {
    if (confirm(`Are you sure you want to remove "${identifierName}" from this identity type?`)) {
      try {
        await deleteIdentityTypeIdentifierMapping(mappingId)
        await loadData()
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to delete mapping')
      }
    }
  }

  const handleCancel = () => {
    setShowAddForm(false)
    resetForm()
  }

  if (loading) return <div>Loading identifier mappings...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  const getIdentifierTypeName = (id: string) => {
    const identifier = availableIdentifiers.find(a => a.id === id)
    return identifier ? identifier.displayName : 'Unknown'
  }

  const getAvailableForAdd = () => {
    const mappedIds = mappings.map(m => m.identifierTypeId)
    return availableIdentifiers.filter(it => !mappedIds.includes(it.id) || (editingMapping && it.id === editingMapping.identifierTypeId))
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h3>Identifier Mappings</h3>
        <button
          onClick={handleAdd}
          style={{
            background: '#059669',
            color: 'white',
            border: 'none',
            padding: '8px 16px',
            borderRadius: 6,
            cursor: 'pointer'
          }}
        >
          Add Identifier
        </button>
      </div>

      <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0f9ff', borderRadius: 8 }}>
        <h4 style={{ margin: '0 0 0.5rem 0' }}>About Identifier Mappings</h4>
        <p style={{ margin: 0, fontSize: '0.875rem', color: '#374151' }}>
          Identifier mappings define which identifier types are available for this identity type. 
          You can set custom validation rules, default values, and specify which identifiers are required or can be used as primary identifiers.
        </p>
      </div>

      {showAddForm && (
        <div style={{ 
          border: '1px solid #e5e7eb', 
          borderRadius: 8, 
          padding: '1rem', 
          marginBottom: '1rem',
          background: '#f9fafb'
        }}>
          <h4>{editingMapping ? 'Edit' : 'Add'} Identifier Mapping</h4>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Identifier Type *
              </label>
              <select
                value={selectedIdentifierType}
                onChange={(e) => setSelectedIdentifierType(e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                required
              >
                <option value="">Select identifier type...</option>
                {getAvailableForAdd().map(identifier => (
                  <option key={identifier.id} value={identifier.id}>
                    {identifier.displayName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Sort Order *
              </label>
              <input
                type="number"
                value={sortOrder}
                onChange={(e) => setSortOrder(parseInt(e.target.value) || 1)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                min="1"
                required
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Override Validation Regex
              </label>
              <input
                type="text"
                value={overrideValidationRegex}
                onChange={(e) => setOverrideValidationRegex(e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Optional: override base validation"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Override Default Value
              </label>
              <input
                type="text"
                value={overrideDefaultValue}
                onChange={(e) => setOverrideDefaultValue(e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Optional: override base default"
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <input
                  type="checkbox"
                  checked={required}
                  onChange={(e) => setRequired(e.target.checked)}
                />
                <span style={{ fontSize: '0.875rem' }}>
                  <strong>Required</strong> - This identifier must be provided for all identities of this type
                </span>
              </label>

              <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <input
                  type="checkbox"
                  checked={primaryCandidate}
                  onChange={(e) => setPrimaryCandidate(e.target.checked)}
                />
                <span style={{ fontSize: '0.875rem' }}>
                  <strong>Primary Candidate</strong> - This identifier can be used as the primary identifier for an identity
                </span>
              </label>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              onClick={handleSave}
              style={{
                background: '#059669',
                color: 'white',
                border: 'none',
                padding: '8px 16px',
                borderRadius: 6,
                cursor: 'pointer'
              }}
            >
              {editingMapping ? 'Update' : 'Add'} Mapping
            </button>
            <button
              onClick={handleCancel}
              style={{
                background: '#6b7280',
                color: 'white',
                border: 'none',
                padding: '8px 16px',
                borderRadius: 6,
                cursor: 'pointer'
              }}
            >
              Cancel
            </button>
          </div>
        </div>
      )}

      {mappings.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '3rem',
          border: '2px dashed #e5e7eb',
          borderRadius: 8,
          color: '#6b7280'
        }}>
          <h4>No identifier mappings configured</h4>
          <p>Add identifier types to define what identifiers can be used with "{identityTypeName}" identities.</p>
        </div>
      ) : (
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
            <thead>
              <tr style={{ background: '#f9fafb' }}>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Order</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Identifier Type</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Data Type</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Validation</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Default Value</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Properties</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {mappings
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((mapping) => (
                <tr key={mapping.id}>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb', textAlign: 'center' }}>
                    <span style={{
                      background: '#f3f4f6',
                      padding: '4px 8px',
                      borderRadius: 4,
                      fontSize: '0.875rem',
                      fontWeight: '500'
                    }}>
                      {mapping.sortOrder}
                    </span>
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                    <div>{mapping.identifierTypeName}</div>
                    {mapping.identifierTypeDescription && (
                      <div style={{ fontSize: '0.75rem', color: '#6b7280', marginTop: '2px' }}>
                        {mapping.identifierTypeDescription}
                      </div>
                    )}
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <span style={{
                      background: '#f3f4f6',
                      padding: '2px 8px',
                      borderRadius: 4,
                      fontSize: '0.75rem',
                      textTransform: 'uppercase'
                    }}>
                      {mapping.identifierDataType}
                    </span>
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    {mapping.effectiveValidationRegex ? (
                      <code style={{ 
                        background: '#f3f4f6', 
                        padding: '2px 4px', 
                        borderRadius: 3,
                        fontSize: '0.75rem'
                      }}>
                        {mapping.effectiveValidationRegex}
                      </code>
                    ) : (
                      <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                    )}
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    {mapping.effectiveDefaultValue || 
                      <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                    }
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '4px' }}>
                      {mapping.required && (
                        <span style={{
                          background: '#fef3c7',
                          color: '#d97706',
                          padding: '2px 6px',
                          borderRadius: 4,
                          fontSize: '0.75rem',
                          fontWeight: '500'
                        }}>
                          Required
                        </span>
                      )}
                      {mapping.primaryCandidate && (
                        <span style={{
                          background: '#dbeafe',
                          color: '#1d4ed8',
                          padding: '2px 6px',
                          borderRadius: 4,
                          fontSize: '0.75rem',
                          fontWeight: '500'
                        }}>
                          Primary
                        </span>
                      )}
                    </div>
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button
                        onClick={() => handleEdit(mapping)}
                        style={{
                          background: 'none',
                          border: 'none',
                          color: '#2563eb',
                          cursor: 'pointer',
                          fontSize: '0.875rem'
                        }}
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(mapping.id, mapping.identifierTypeName)}
                        style={{
                          background: 'none',
                          border: 'none',
                          color: '#dc2626',
                          cursor: 'pointer',
                          fontSize: '0.875rem'
                        }}
                      >
                        Remove
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
