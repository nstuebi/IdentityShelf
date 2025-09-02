import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { IdentifierType, getIdentifierType, createIdentifierType, updateIdentifierType } from '../api/client'

const DATA_TYPES = [
  'STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'DATE', 'DATETIME', 'EMAIL', 'PHONE', 'URL'
]

export default function IdentifierTypeForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const mode = id ? 'edit' : 'create'
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [testValue, setTestValue] = useState('')
  const [regexTestResult, setRegexTestResult] = useState<{ isValid: boolean; message: string } | null>(null)
  
  const [formData, setFormData] = useState<Partial<IdentifierType>>({
    name: '',
    displayName: '',
    description: '',
    dataType: 'STRING',
    defaultValue: '',
    validationRegex: '',
    unique: true,
    searchable: true,
    active: true
  })

  useEffect(() => {
    if (mode === 'edit' && id) {
      loadIdentifierType(id)
    }
  }, [mode, id])

  const loadIdentifierType = async (identifierTypeId: string) => {
    try {
      setLoading(true)
      const identifierType = await getIdentifierType(identifierTypeId)
      setFormData(identifierType)
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
        await createIdentifierType({
          name: formData.name,
          displayName: formData.displayName,
          description: formData.description || '',
          dataType: formData.dataType,
          defaultValue: formData.defaultValue || '',
          validationRegex: formData.validationRegex || '',
          unique: formData.unique !== false,
          searchable: formData.searchable !== false
        })
      } else if (mode === 'edit' && id) {
        await updateIdentifierType(id, {
          name: formData.name!,
          displayName: formData.displayName!,
          description: formData.description || '',
          dataType: formData.dataType!,
          defaultValue: formData.defaultValue || '',
          validationRegex: formData.validationRegex || '',
          unique: formData.unique !== false,
          searchable: formData.searchable !== false
        })
      }

      navigate('/identifier-types')
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

  if (loading && mode === 'edit') return <div>Loading...</div>

  return (
    <div>
      <h1>{mode === 'create' ? 'Create Identifier Type' : 'Edit Identifier Type'}</h1>
      
      <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0f9ff', borderRadius: 8 }}>
        <h3 style={{ margin: '0 0 0.5rem 0' }}>Searchable Identifier Types</h3>
        <p style={{ margin: 0, fontSize: '0.875rem', color: '#374151' }}>
          Define searchable identity markers like SSN, passport numbers, or driver licenses. 
          These are highly indexed for fast lookups and can enforce uniqueness and validation patterns.
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
              placeholder="e.g., ssn, passport"
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
              placeholder="e.g., Social Security Number"
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
            placeholder="Description of this identifier type"
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
              placeholder="Optional default value"
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
              placeholder="Enter regex pattern, e.g., ^\d{3}-?\d{2}-?\d{4}$ for SSN"
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
              <strong>Common identifier regex examples:</strong>
            </div>
            <div style={{ 
              fontFamily: 'monospace', 
              background: '#f9fafb', 
              padding: '8px', 
              borderRadius: '4px',
              border: '1px solid #e5e7eb',
              marginBottom: '6px'
            }}>
              <div>• SSN: <code>^\d{'{3}'}-?\d{'{2}'}-?\d{'{4}'}$</code></div>
              <div>• Passport: <code>^[A-Z0-9]{'{6,15}'}$</code></div>
              <div>• Driver License: <code>^[A-Z0-9]{'{8,20}'}$</code></div>
              <div>• Employee ID: <code>^EMP\d{'{6}'}$</code></div>
            </div>
            <div>
              <strong>Note:</strong> This is the base validation. Identity types can add additional validation rules when mapping this identifier.
            </div>
          </div>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <input
              type="checkbox"
              checked={formData.unique}
              onChange={(e) => setFormData({ ...formData, unique: e.target.checked })}
            />
            Unique (prevent duplicate values)
          </label>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <input
              type="checkbox"
              checked={formData.searchable}
              onChange={(e) => setFormData({ ...formData, searchable: e.target.checked })}
            />
            Searchable (enable fast search indexing)
          </label>
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
            {loading ? 'Saving...' : mode === 'create' ? 'Create Identifier Type' : 'Update Identifier Type'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/identifier-types')}
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
