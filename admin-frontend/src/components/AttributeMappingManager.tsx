import { useState, useEffect } from 'react'
import { 
  IdentityTypeAttributeMapping, 
  AttributeType, 
  getMappingsForIdentityType, 
  listAttributeTypes,
  createMapping, 
  updateMapping, 
  deleteMapping 
} from '../api/client'

interface AttributeMappingManagerProps {
  identityTypeId: string
  identityTypeName: string
  onMappingsChange: (mappings: IdentityTypeAttributeMapping[]) => void
}

export default function AttributeMappingManager({ 
  identityTypeId, 
  identityTypeName, 
  onMappingsChange 
}: AttributeMappingManagerProps) {
  const [mappings, setMappings] = useState<IdentityTypeAttributeMapping[]>([])
  const [availableAttributes, setAvailableAttributes] = useState<AttributeType[]>([])
  const [showAddForm, setShowAddForm] = useState(false)
  const [editingMapping, setEditingMapping] = useState<IdentityTypeAttributeMapping | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  
  // Form state for adding/editing mappings
  const [formData, setFormData] = useState({
    attributeTypeId: '',
    sortOrder: 0,
    required: false,
    overrideValidationRegex: '',
    overrideDefaultValue: ''
  })

  useEffect(() => {
    loadData()
  }, [identityTypeId])

  const loadData = async () => {
    try {
      setLoading(true)
      const [mappingsData, attributeTypesData] = await Promise.all([
        getMappingsForIdentityType(identityTypeId),
        listAttributeTypes()
      ])
      setMappings(mappingsData)
      setAvailableAttributes(attributeTypesData)
      onMappingsChange(mappingsData)
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

  const resetForm = () => {
    setFormData({
      attributeTypeId: '',
      sortOrder: Math.max(...mappings.map(m => m.sortOrder), -1) + 1,
      required: false,
      overrideValidationRegex: '',
      overrideDefaultValue: ''
    })
  }

  const handleAdd = () => {
    resetForm()
    setEditingMapping(null)
    setShowAddForm(true)
  }

  const handleEdit = (mapping: IdentityTypeAttributeMapping) => {
    setFormData({
      attributeTypeId: mapping.attributeTypeId,
      sortOrder: mapping.sortOrder,
      required: mapping.required,
      overrideValidationRegex: mapping.overrideValidationRegex || '',
      overrideDefaultValue: mapping.overrideDefaultValue || ''
    })
    setEditingMapping(mapping)
    setShowAddForm(true)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!formData.attributeTypeId) {
      setError('Please select an attribute type')
      return
    }

    try {
      setError(null)
      if (editingMapping) {
        await updateMapping(editingMapping.id, {
          sortOrder: formData.sortOrder,
          required: formData.required,
          overrideValidationRegex: formData.overrideValidationRegex || undefined,
          overrideDefaultValue: formData.overrideDefaultValue || undefined
        })
      } else {
        await createMapping({
          identityTypeId,
          attributeTypeId: formData.attributeTypeId,
          sortOrder: formData.sortOrder,
          required: formData.required,
          overrideValidationRegex: formData.overrideValidationRegex || undefined,
          overrideDefaultValue: formData.overrideDefaultValue || undefined
        })
      }
      setShowAddForm(false)
      setEditingMapping(null)
      await loadData()
    } catch (err) {
      setError(String(err))
    }
  }

  const handleDelete = async (mappingId: string, attributeName: string) => {
    if (confirm(`Remove "${attributeName}" from this identity type?`)) {
      try {
        await deleteMapping(mappingId)
        await loadData()
      } catch (err) {
        setError(String(err))
      }
    }
  }

  const getAvailableAttributesForAdd = () => {
    const mappedAttributeIds = new Set(mappings.map(m => m.attributeTypeId))
    return availableAttributes.filter(attr => !mappedAttributeIds.has(attr.id) && attr.active)
  }

  if (loading) return <div>Loading mappings...</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h3>Attribute Mappings for {identityTypeName}</h3>
        {!showAddForm && (
          <button
            onClick={handleAdd}
            style={{
              background: '#059669',
              color: 'white',
              border: 'none',
              padding: '6px 12px',
              borderRadius: 6,
              cursor: 'pointer'
            }}
          >
            Add Attribute
          </button>
        )}
      </div>

      {error && (
        <div style={{
          background: '#fef2f2',
          border: '1px solid #fecaca',
          color: '#dc2626',
          padding: '12px',
          borderRadius: 6,
          marginBottom: '1rem'
        }}>
          {error}
        </div>
      )}

      {showAddForm && (
        <form onSubmit={handleSubmit} style={{ 
          border: '1px solid #e5e7eb', 
          borderRadius: 8, 
          padding: '1rem', 
          marginBottom: '1rem',
          background: '#f9fafb'
        }}>
          <h4>{editingMapping ? 'Edit Attribute Mapping' : 'Add Attribute Mapping'}</h4>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Attribute Type *
              </label>
              <select
                value={formData.attributeTypeId}
                onChange={(e) => setFormData({ ...formData, attributeTypeId: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                disabled={!!editingMapping}
                required
              >
                <option value="">Select an attribute type...</option>
                {(editingMapping ? availableAttributes : getAvailableAttributesForAdd()).map(attr => (
                  <option key={attr.id} value={attr.id}>
                    {attr.displayName} ({attr.name}) - {attr.dataType}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Sort Order
              </label>
              <input
                type="number"
                value={formData.sortOrder}
                onChange={(e) => setFormData({ ...formData, sortOrder: parseInt(e.target.value) || 0 })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                min="0"
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Override Default Value
              </label>
              <input
                type="text"
                value={formData.overrideDefaultValue}
                onChange={(e) => setFormData({ ...formData, overrideDefaultValue: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Leave empty to use base default"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Additional Validation Regex
              </label>
              <input
                type="text"
                value={formData.overrideValidationRegex}
                onChange={(e) => setFormData({ ...formData, overrideValidationRegex: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Additional validation (cumulative)"
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.required}
                onChange={(e) => setFormData({ ...formData, required: e.target.checked })}
              />
              Required for this identity type
            </label>
          </div>

          <div style={{ fontSize: '0.75rem', color: '#6b7280', marginBottom: '1rem' }}>
            <strong>Note:</strong> Additional validation regex will be checked in addition to the base validation. 
            Both rules must pass for the value to be valid.
          </div>

          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              type="submit"
              style={{
                background: '#2563eb',
                color: 'white',
                border: 'none',
                padding: '8px 16px',
                borderRadius: 6,
                cursor: 'pointer'
              }}
            >
              {editingMapping ? 'Update Mapping' : 'Add Mapping'}
            </button>
            <button
              type="button"
              onClick={() => {
                setShowAddForm(false)
                setEditingMapping(null)
                setError(null)
              }}
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
        </form>
      )}

      {mappings.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '2rem',
          border: '2px dashed #e5e7eb',
          borderRadius: 8,
          color: '#6b7280'
        }}>
          No attributes mapped yet. Add your first attribute to get started!
        </div>
      ) : (
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
            <thead>
              <tr style={{ background: '#f9fafb' }}>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Order</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Attribute</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Type</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Required</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Default Value</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Validation</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {mappings
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((mapping) => (
                  <tr key={mapping.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {mapping.sortOrder}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                      <div>{mapping.attributeTypeDisplayName}</div>
                      <div style={{ fontSize: '0.75rem', color: '#6b7280' }}>
                        {mapping.attributeTypeName}
                      </div>
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      <span style={{
                        background: '#f3f4f6',
                        padding: '2px 6px',
                        borderRadius: 3,
                        fontSize: '0.75rem'
                      }}>
                        {mapping.attributeDataType}
                      </span>
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {mapping.required ? (
                        <span style={{ color: '#dc2626', fontWeight: '500' }}>Yes</span>
                      ) : (
                        <span style={{ color: '#6b7280' }}>No</span>
                      )}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      <div style={{ fontSize: '0.875rem' }}>
                        {mapping.effectiveDefaultValue || 
                          <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                        }
                      </div>
                      {mapping.overrideDefaultValue && (
                        <div style={{ fontSize: '0.75rem', color: '#059669' }}>
                          Override: {mapping.overrideDefaultValue}
                        </div>
                      )}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      <div style={{ fontSize: '0.75rem' }}>
                        {mapping.baseValidationRegex && (
                          <div style={{ marginBottom: '4px' }}>
                            <strong>Base:</strong> <code style={{ background: '#f3f4f6', padding: '1px 3px' }}>
                              {mapping.baseValidationRegex.length > 20 
                                ? mapping.baseValidationRegex.substring(0, 20) + '...' 
                                : mapping.baseValidationRegex}
                            </code>
                          </div>
                        )}
                        {mapping.overrideValidationRegex && (
                          <div>
                            <strong style={{ color: '#059669' }}>Additional:</strong> <code style={{ background: '#f0f9ff', padding: '1px 3px' }}>
                              {mapping.overrideValidationRegex.length > 20 
                                ? mapping.overrideValidationRegex.substring(0, 20) + '...' 
                                : mapping.overrideValidationRegex}
                            </code>
                          </div>
                        )}
                        {!mapping.baseValidationRegex && !mapping.overrideValidationRegex && (
                          <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                        )}
                      </div>
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
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
                          onClick={() => handleDelete(mapping.id, mapping.attributeTypeDisplayName)}
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
