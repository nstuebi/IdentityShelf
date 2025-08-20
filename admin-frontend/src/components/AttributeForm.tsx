import { FormEvent, useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { AttributeType, getAttributeType, createAttributeType, updateAttributeType, getIdentityType } from '../api/client'

export default function AttributeForm({ mode }: { mode: 'create' | 'edit' }) {
  const navigate = useNavigate()
  const { typeName, attributeId } = useParams()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [formData, setFormData] = useState({
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

  useEffect(() => {
    if (mode === 'edit' && attributeId) {
      loadAttribute()
    }
  }, [mode, attributeId])

  async function loadAttribute() {
    try {
      setLoading(true)
      const attribute = await getAttributeType(attributeId!)
      setFormData({
        name: attribute.name,
        displayName: attribute.displayName,
        description: attribute.description || '',
        dataType: attribute.dataType,
        required: attribute.required,
        defaultValue: attribute.defaultValue || '',
        validationRegex: attribute.validationRegex || '',
        sortOrder: attribute.sortOrder,
        active: attribute.active
      })
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load attribute')
    } finally {
      setLoading(false)
    }
  }

  function handleInputChange(field: string, value: any) {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      if (mode === 'create') {
        // Get the identity type to get its actual ID
        const identityType = await getIdentityType(typeName!)
        await createAttributeType({
          ...formData,
          identityTypeId: identityType.id
        })
        alert('Attribute created successfully!')
      } else {
        await updateAttributeType(attributeId!, formData)
        alert('Attribute updated successfully!')
      }
      navigate(`/types/${typeName}/view`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to save attribute')
    } finally {
      setLoading(false)
    }
  }

  function handleCancel() {
    if (confirm('Are you sure you want to cancel? All unsaved changes will be lost.')) {
      navigate(`/types/${typeName}/view`)
    }
  }

  if (loading) return <div>Loading attribute...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>{mode === 'create' ? 'Add New Attribute' : 'Edit Attribute'}</h2>
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

      <form onSubmit={handleSubmit} style={{ maxWidth: '800px' }}>
        <div style={{ 
          border: '1px solid #e5e7eb', 
          borderRadius: 8, 
          padding: '1.5rem', 
          marginBottom: '1.5rem',
          background: 'white'
        }}>
          <h3>Attribute Information</h3>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Name *
              </label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => handleInputChange('name', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., phone_number"
                disabled={mode === 'edit'} // Name cannot be changed after creation
                required
              />
              {mode === 'edit' && (
                <small style={{ color: '#6b7280' }}>Name cannot be changed after creation</small>
              )}
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Display Name *
              </label>
              <input
                type="text"
                value={formData.displayName}
                onChange={(e) => handleInputChange('displayName', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., Phone Number"
                required
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
              Description
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => handleInputChange('description', e.target.value)}
              style={{
                width: '100%',
                padding: '8px',
                border: '1px solid #d1d5db',
                borderRadius: 4,
                minHeight: '80px'
              }}
              placeholder="Describe what this attribute represents..."
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Data Type *
              </label>
              <select
                value={formData.dataType}
                onChange={(e) => handleInputChange('dataType', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                required
              >
                <option value="STRING">String</option>
                <option value="INTEGER">Integer</option>
                <option value="DECIMAL">Decimal</option>
                <option value="BOOLEAN">Boolean</option>
                <option value="DATE">Date</option>
                <option value="DATETIME">DateTime</option>
              </select>
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Sort Order
              </label>
              <input
                type="number"
                value={formData.sortOrder}
                onChange={(e) => handleInputChange('sortOrder', parseInt(e.target.value))}
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
                Default Value
              </label>
              <input
                type="text"
                value={formData.defaultValue}
                onChange={(e) => handleInputChange('defaultValue', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="Leave empty for no default"
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Validation Regex
              </label>
              <input
                type="text"
                value={formData.validationRegex}
                onChange={(e) => handleInputChange('validationRegex', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., ^[a-zA-Z0-9_]{3,20}$"
              />
            </div>
          </div>

          <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', marginBottom: '1rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.required}
                onChange={(e) => handleInputChange('required', e.target.checked)}
              />
              <span style={{ fontWeight: '500' }}>Required</span>
            </label>

            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.active}
                onChange={(e) => handleInputChange('active', e.target.checked)}
              />
              <span style={{ fontWeight: '500' }}>Active</span>
            </label>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '8px' }}>
          <button 
            type="submit" 
            disabled={loading}
            style={{ 
              background: '#2563eb', 
              color: 'white', 
              border: 'none', 
              padding: '10px 16px', 
              borderRadius: 6,
              fontSize: '14px',
              fontWeight: '500',
              cursor: loading ? 'not-allowed' : 'pointer',
              opacity: loading ? 0.6 : 1
            }}
          >
            {loading ? 'Saving...' : (mode === 'create' ? 'Create Attribute' : 'Update Attribute')}
          </button>
        </div>
      </form>
    </div>
  )
}
