import { useState } from 'react'
import { AttributeType } from '../api/client'

interface AttributeTypeFormProps {
  attributes: AttributeType[]
  onAttributesChange: (attributes: AttributeType[]) => void
}

const DATA_TYPES = [
  'STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'DATE', 'DATETIME', 'EMAIL', 'PHONE', 'URL', 'SELECT', 'MULTI_SELECT'
]



export default function AttributeTypeForm({ attributes, onAttributesChange }: AttributeTypeFormProps) {
  const [showForm, setShowForm] = useState(false)
  const [editingIndex, setEditingIndex] = useState<number | null>(null)
  const [formData, setFormData] = useState<Partial<AttributeType>>({
    name: '',
    displayName: '',
    description: '',
    dataType: 'STRING',
    required: false,
    defaultValue: '',
    validationRegex: '',
    sortOrder: 0,
    active: true
  })
  
  // Regex testing state
  const [testValue, setTestValue] = useState('')
  const [regexTestResult, setRegexTestResult] = useState<{ isValid: boolean; message: string } | null>(null)

  // Test regex pattern
  const testRegex = () => {
    if (!formData.validationRegex) {
      setRegexTestResult(null)
      return
    }

    try {
      const regex = new RegExp(formData.validationRegex)
      const isValid = regex.test(testValue)
      setRegexTestResult({
        isValid,
        message: isValid ? 'Pattern matches!' : 'Pattern does not match'
      })
    } catch (error) {
      setRegexTestResult({
        isValid: false,
        message: `Invalid regex: ${error instanceof Error ? error.message : 'Unknown error'}`
      })
    }
  }



  // Clear regex test results when regex changes
  const handleRegexChange = (value: string) => {
    setFormData({ ...formData, validationRegex: value })
    setRegexTestResult(null)
  }

  function handleAddAttribute() {
    if (!formData.name || !formData.displayName || !formData.dataType) {
      alert('Please fill in all required fields')
      return
    }

    const newAttribute: AttributeType = {
      id: `temp-${Date.now()}`, // Temporary ID for new attributes
      name: formData.name,
      displayName: formData.displayName,
      description: formData.description || '',
      dataType: formData.dataType,
      required: formData.required || false,
      defaultValue: formData.defaultValue || '',
      validationRegex: formData.validationRegex || '',
      sortOrder: formData.sortOrder || 0,
      active: formData.active !== false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }

    const newAttributes = [...attributes, newAttribute]
    onAttributesChange(newAttributes)
    resetForm()
    setShowForm(false)
  }

  function handleEditAttribute(index: number) {
    const attribute = attributes[index]
    setFormData({
      name: attribute.name,
      displayName: attribute.displayName,
      description: attribute.description,
      dataType: attribute.dataType,
      required: attribute.required,
      defaultValue: attribute.defaultValue,
      validationRegex: attribute.validationRegex,
      sortOrder: attribute.sortOrder,
      active: attribute.active
    })
    setEditingIndex(index)
    setShowForm(true)
  }

  function handleUpdateAttribute() {
    if (!formData.name || !formData.displayName || !formData.dataType || editingIndex === null) {
      alert('Please fill in all required fields')
      return
    }

    const updatedAttributes = [...attributes]
    updatedAttributes[editingIndex] = {
      ...updatedAttributes[editingIndex],
      name: formData.name,
      displayName: formData.displayName,
      description: formData.description || '',
      dataType: formData.dataType,
      required: formData.required || false,
      defaultValue: formData.defaultValue || '',
      validationRegex: formData.validationRegex || '',
      sortOrder: formData.sortOrder || 0,
      active: formData.active !== false,
      updatedAt: new Date().toISOString()
    }

    onAttributesChange(updatedAttributes)
    resetForm()
    setShowForm(false)
    setEditingIndex(null)
  }

  function handleDeleteAttribute(index: number) {
    if (confirm('Are you sure you want to delete this attribute?')) {
      const newAttributes = attributes.filter((_, i) => i !== index)
      onAttributesChange(newAttributes)
    }
  }

  function handleToggleAttribute(index: number) {
    const newAttributes = [...attributes]
    newAttributes[index] = {
      ...newAttributes[index],
      active: !newAttributes[index].active
    }
    onAttributesChange(newAttributes)
  }

  function resetForm() {
    setFormData({
      name: '',
      displayName: '',
      description: '',
      dataType: 'STRING',
      required: false,
      defaultValue: '',
      validationRegex: '',
      sortOrder: 0,
      active: true
    })
    setTestValue('')
    setRegexTestResult(null)
  }

  function cancelForm() {
    resetForm()
    setShowForm(false)
    setEditingIndex(null)
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h3>Attributes</h3>
        {!showForm && (
          <button
            onClick={() => setShowForm(true)}
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

      {showForm && (
        <div style={{ 
          border: '1px solid #e5e7eb', 
          borderRadius: 8, 
          padding: '1rem', 
          marginBottom: '1rem',
          background: '#f9fafb'
        }}>
          <h4>{editingIndex !== null ? 'Edit Attribute' : 'New Attribute'}</h4>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Name *
              </label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., employee_id"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Display Name *
              </label>
              <input
                type="text"
                value={formData.displayName}
                onChange={(e) => setFormData({ ...formData, displayName: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., Employee ID"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Data Type *
              </label>
              <select
                value={formData.dataType}
                onChange={(e) => setFormData({ ...formData, dataType: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
              >
                {DATA_TYPES.map(type => (
                  <option key={type} value={type}>{type}</option>
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

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
              Description
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              style={{
                width: '100%',
                padding: '8px',
                border: '1px solid #d1d5db',
                borderRadius: 4,
                minHeight: '60px'
              }}
              placeholder="Description of this attribute"
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Default Value
              </label>
              <input
                type="text"
                value={formData.defaultValue}
                onChange={(e) => setFormData({ ...formData, defaultValue: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Default value for this attribute"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Validation Regex
              </label>
              <div style={{ marginBottom: '8px' }}>
                <input
                  type="text"
                  value={formData.validationRegex}
                  onChange={(e) => handleRegexChange(e.target.value)}
                  style={{
                    width: '100%',
                    padding: '8px',
                    border: '1px solid #d1d5db',
                    borderRadius: 4
                  }}
                  placeholder="e.g., ^[a-zA-Z0-9_]{3,20}$"
                />
              </div>
              


              {/* Regex testing */}
              {formData.validationRegex && (
                <div style={{ 
                  border: '1px solid #e5e7eb', 
                  borderRadius: '4px', 
                  padding: '12px', 
                  background: '#f9fafb',
                  marginBottom: '8px'
                }}>
                  <div style={{ fontSize: '0.875rem', fontWeight: '500', marginBottom: '8px' }}>
                    Test your regex pattern:
                  </div>
                  <div style={{ display: 'flex', gap: '8px', marginBottom: '8px' }}>
                    <input
                      type="text"
                      value={testValue}
                      onChange={(e) => setTestValue(e.target.value)}
                      placeholder="Enter test value"
                      style={{
                        flex: 1,
                        padding: '6px 8px',
                        border: '1px solid #d1d5db',
                        borderRadius: '4px',
                        fontSize: '0.875rem'
                      }}
                    />
                    <button
                      type="button"
                      onClick={testRegex}
                      style={{
                        background: '#2563eb',
                        color: 'white',
                        border: 'none',
                        padding: '6px 12px',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '0.875rem'
                      }}
                    >
                      Test
                    </button>
                  </div>
                  {regexTestResult && (
                    <div style={{ 
                      fontSize: '0.875rem', 
                      color: regexTestResult.isValid ? '#166534' : '#dc2626',
                      fontWeight: '500'
                    }}>
                      {regexTestResult.message}
                    </div>
                  )}
                </div>
              )}

              {/* Enhanced help text with examples */}
              <div style={{ fontSize: '0.75rem', color: '#6b7280', marginTop: '8px' }}>
                <div style={{ marginBottom: '6px' }}>
                  <strong>Common regex examples:</strong>
                </div>
                <div style={{ 
                  fontFamily: 'monospace', 
                  background: '#f9fafb', 
                  padding: '8px', 
                  borderRadius: '4px',
                  border: '1px solid #e5e7eb',
                  marginBottom: '6px'
                }}>
                  <div>• Username: <code>^[a-zA-Z0-9_]{'{3,20}'}$</code></div>
                  <div>• Email: <code>^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{'{2,}'}$</code></div>
                  <div>• Phone: <code>^\+?[\d\s\-\(\)]{'{7,20}'}$</code></div>
                  <div>• Date (YYYY-MM-DD): <code>^\d{'{4}'}-\d{'{2}'}-\d{'{2}'}$</code></div>
                  <div>• Postal Code: <code>^\d{'{5}'}(-\d{'{4}'})?$</code></div>
                </div>
                <div>
                  <strong>Tips:</strong> Use ^ and $ for exact matches, [a-z] for ranges, {'{3,20}'} for length, \d for digits, \w for word characters.
                </div>
              </div>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '8px', marginBottom: '1rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.required}
                onChange={(e) => setFormData({ ...formData, required: e.target.checked })}
              />
              Required
            </label>

            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.active}
                onChange={(e) => setFormData({ ...formData, active: e.target.checked })}
              />
              Active
            </label>
          </div>

          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              onClick={editingIndex !== null ? handleUpdateAttribute : handleAddAttribute}
              style={{
                background: '#2563eb',
                color: 'white',
                border: 'none',
                padding: '8px 16px',
                borderRadius: 6,
                cursor: 'pointer'
              }}
            >
              {editingIndex !== null ? 'Update' : 'Add'} Attribute
            </button>
            <button
              onClick={cancelForm}
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

      {attributes.length > 0 && (
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
            <thead>
              <tr style={{ background: '#f9fafb' }}>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Name</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Display Name</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Type</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Required</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Order</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Validation</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Status</th>
                <th style={{ padding: '8px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {attributes
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((attribute, index) => (
                  <tr key={attribute.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                      {attribute.name}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {attribute.displayName}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {attribute.dataType.toLowerCase()}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {attribute.required ? 'Yes' : 'No'}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {attribute.sortOrder}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      {attribute.validationRegex ? attribute.validationRegex : 'N/A'}
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      <button
                        onClick={() => handleToggleAttribute(index)}
                        style={{
                          background: attribute.active ? '#dcfce7' : '#fef2f2',
                          color: attribute.active ? '#166534' : '#dc2626',
                          border: 'none',
                          padding: '4px 8px',
                          borderRadius: 4,
                          fontSize: '0.875rem',
                          cursor: 'pointer'
                        }}
                      >
                        {attribute.active ? 'Active' : 'Inactive'}
                      </button>
                    </td>
                    <td style={{ padding: '8px', border: '1px solid #e5e7eb' }}>
                      <button
                        onClick={() => handleEditAttribute(index)}
                        style={{
                          background: 'none',
                          border: 'none',
                          color: '#2563eb',
                          cursor: 'pointer',
                          marginRight: '8px'
                        }}
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteAttribute(index)}
                        style={{
                          background: 'none',
                          border: 'none',
                          color: '#dc2626',
                          cursor: 'pointer'
                        }}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      )}

      {attributes.length === 0 && !showForm && (
        <div style={{ textAlign: 'center', padding: '2rem', color: '#6b7280', border: '2px dashed #e5e7eb', borderRadius: 8 }}>
          No attributes defined yet. Add your first attribute to get started!
        </div>
      )}
    </div>
  )
}
