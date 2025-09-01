import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { createAttributeType, updateAttributeType, getAttributeType, AttributeType } from '../api/client'

const DATA_TYPES = [
  'STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'DATE', 'DATETIME', 'EMAIL', 'PHONE', 'URL', 'SELECT', 'MULTI_SELECT'
]

export default function AttributeTypeForm({ mode }: { mode: 'create' | 'edit' }) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [testValue, setTestValue] = useState('')
  const [regexTestResult, setRegexTestResult] = useState<{ isValid: boolean; message: string } | null>(null)
  
  const [formData, setFormData] = useState<Partial<AttributeType>>({
    name: '',
    displayName: '',
    description: '',
    dataType: 'STRING',
    defaultValue: '',
    validationRegex: '',
    active: true
  })

  useEffect(() => {
    if (mode === 'edit' && id) {
      loadAttributeType(id)
    }
  }, [mode, id])

  const loadAttributeType = async (attributeId: string) => {
    try {
      setLoading(true)
      const attributeType = await getAttributeType(attributeId)
      setFormData(attributeType)
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

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

  const handleRegexChange = (value: string) => {
    setFormData({ ...formData, validationRegex: value })
    setRegexTestResult(null)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!formData.name || !formData.displayName || !formData.dataType) {
      setError('Please fill in all required fields')
      return
    }

    try {
      setLoading(true)
      setError(null)

      if (mode === 'create') {
        await createAttributeType({
          name: formData.name,
          displayName: formData.displayName,
          description: formData.description || '',
          dataType: formData.dataType,
          defaultValue: formData.defaultValue || '',
          validationRegex: formData.validationRegex || '',
          active: formData.active !== false
        })
      } else if (mode === 'edit' && id) {
        await updateAttributeType(id, formData)
      }

      navigate('/attribute-types')
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

  if (loading && mode === 'edit') return <div>Loading...</div>

  return (
    <div>
      <h1>{mode === 'create' ? 'Create Attribute Type' : 'Edit Attribute Type'}</h1>
      
      <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0f9ff', borderRadius: 8 }}>
        <h3 style={{ margin: '0 0 0.5rem 0' }}>Independent Attribute Types</h3>
        <p style={{ margin: 0, fontSize: '0.875rem', color: '#374151' }}>
          Define reusable attribute types that can be mapped to multiple identity types. 
          Each mapping can override the default validation and values as needed.
        </p>
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

      <form onSubmit={handleSubmit} style={{ maxWidth: '600px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
              Name * (internal identifier)
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
              placeholder="e.g., email_address"
              required
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
              placeholder="e.g., Email Address"
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
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            style={{
              width: '100%',
              padding: '8px',
              border: '1px solid #d1d5db',
              borderRadius: 4,
              minHeight: '60px'
            }}
            placeholder="Description of this attribute type"
          />
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
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
              required
            >
              {DATA_TYPES.map(type => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </div>

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
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
            Base Validation Regex
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
              placeholder="Enter regex pattern, e.g., ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
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
              <div>• Email: <code>^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{'{2,}'}$</code></div>
              <div>• Phone: <code>^\+?[\d\s\-\(\)]{'{7,20}'}$</code></div>
              <div>• Username: <code>^[a-zA-Z0-9_]{'{3,20}'}$</code></div>
              <div>• Postal Code: <code>^\d{'{5}'}(-\d{'{4}'})?$</code></div>
            </div>
            <div>
              <strong>Note:</strong> This is the base validation. Identity types can add additional validation rules when mapping this attribute.
            </div>
          </div>
        </div>

        <div style={{ marginBottom: '1rem' }}>
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
            type="submit"
            disabled={loading}
            style={{
              background: '#2563eb',
              color: 'white',
              border: 'none',
              padding: '8px 16px',
              borderRadius: 6,
              cursor: loading ? 'not-allowed' : 'pointer',
              opacity: loading ? 0.6 : 1
            }}
          >
            {loading ? 'Saving...' : mode === 'create' ? 'Create Attribute Type' : 'Update Attribute Type'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/attribute-types')}
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
    </div>
  )
}
