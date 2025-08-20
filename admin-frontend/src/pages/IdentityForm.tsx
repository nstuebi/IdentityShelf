import { FormEvent, useEffect, useState } from 'react'
import { createIdentity, getIdentity, updateIdentity, listIdentityTypes, IdentityType, AttributeType } from '../api/client'
import { useNavigate, useParams } from 'react-router-dom'

export default function IdentityForm({ mode }: { mode: 'create' | 'edit' }) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [selectedIdentityType, setSelectedIdentityType] = useState<string>('')
  const [identityTypes, setIdentityTypes] = useState<IdentityType[]>([])
  const [attributes, setAttributes] = useState<AttributeType[]>([])
  const [attributeValues, setAttributeValues] = useState<Record<string, any>>({})
  const [error, setError] = useState<string | null>(null)
  const [errorDetails, setErrorDetails] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    // Load identity types for create mode
    if (mode === 'create') {
      listIdentityTypes()
        .then(setIdentityTypes)
        .catch((e) => setError(String(e)))
    }
  }, [mode])

  useEffect(() => {
    if (mode === 'edit' && id) {
      getIdentity(id).then((i) => {
        setUsername(i.username)
        setEmail(i.email)
        setFirstName(i.firstName ?? '')
        setLastName(i.lastName ?? '')
        // TODO: Load identity type and attributes for edit mode
      }).catch((e) => setError(String(e)))
    }
  }, [mode, id])

  // When identity type changes, load its attributes
  useEffect(() => {
    if (selectedIdentityType) {
      const selectedType = identityTypes.find((t: IdentityType) => t.id === selectedIdentityType)
      if (selectedType) {
        setAttributes(selectedType.attributes)
        // Initialize attribute values with defaults
        const initialValues: Record<string, any> = {}
        selectedType.attributes.forEach((attr: AttributeType) => {
          if (attr.defaultValue) {
            initialValues[attr.name] = attr.defaultValue
          } else {
            initialValues[attr.name] = ''
          }
        })
        setAttributeValues(initialValues)
      }
    } else {
      setAttributes([])
      setAttributeValues({})
    }
  }, [selectedIdentityType, identityTypes])

  const handleAttributeChange = (attributeName: string, value: any) => {
    setAttributeValues((prev: Record<string, any>) => ({
      ...prev,
      [attributeName]: value
    }))
  }

  const renderAttributeField = (attribute: AttributeType) => {
    const value = attributeValues[attribute.name] || ''
    
    switch (attribute.dataType) {
      case 'BOOLEAN':
        return (
          <input
            type="checkbox"
            checked={value === true || value === 'true'}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.checked)}
            required={attribute.required}
          />
        )
      case 'INTEGER':
      case 'DECIMAL':
        return (
          <input
            type="number"
            value={value}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.value)}
            required={attribute.required}
            step={attribute.dataType === 'DECIMAL' ? '0.01' : '1'}
          />
        )
      case 'DATE':
        return (
          <input
            type="date"
            value={value}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.value)}
            required={attribute.required}
          />
        )
      case 'DATETIME':
        return (
          <input
            type="datetime-local"
            value={value}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.value)}
            required={attribute.required}
          />
        )
      case 'EMAIL':
        return (
          <input
            type="email"
            value={value}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.value)}
            required={attribute.required}
            maxLength={320}
          />
        )
      default: // STRING, PHONE, URL, SELECT, MULTI_SELECT
        return (
          <input
            type="text"
            value={value}
            onChange={(e) => handleAttributeChange(attribute.name, e.target.value)}
            required={attribute.required}
            maxLength={255}
          />
        )
    }
  }

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    
    // Validate required fields
    if (mode === 'create' && !selectedIdentityType) {
      setError('Please select an identity type')
      setLoading(false)
      return
    }
    
    if (!username.trim()) {
      setError('Username is required')
      setLoading(false)
      return
    }
    
    if (!email.trim()) {
      setError('Email is required')
      setLoading(false)
      return
    }
    
    // Validate required attributes
    const missingRequiredAttributes = attributes
      .filter(attr => attr.required)
      .filter(attr => {
        const value = attributeValues[attr.name]
        return value === undefined || value === null || value === ''
      })
    
    if (missingRequiredAttributes.length > 0) {
      setError(`Please fill in all required attributes: ${missingRequiredAttributes.map(attr => attr.displayName).join(', ')}`)
      setLoading(false)
      return
    }
    
    try {
      if (mode === 'create') {
        await createIdentity({ 
          username, 
          email, 
          firstName, 
          lastName, 
          identityType: selectedIdentityType,
          attributes: attributeValues
        })
      } else if (id) {
        await updateIdentity(id, { username, email, firstName, lastName })
      }
      navigate('/')
    } catch (e) {
      setError(String(e))
      // Capture detailed error information
      if (e instanceof Error) {
        setErrorDetails({
          message: e.message,
          name: e.name,
          stack: e.stack
        })
      } else if (typeof e === 'object' && e !== null) {
        setErrorDetails(e)
      } else {
        setErrorDetails({ raw: e })
      }
    } finally {
      setLoading(false)
    }
  }

  if (mode === 'create' && identityTypes.length === 0) {
    return <div>Loading identity types...</div>
  }

  if (mode === 'create' && identityTypes.length === 0 && !loading) {
    return (
      <div style={{ textAlign: 'center', padding: '2rem' }}>
        <h2>No Identity Types Available</h2>
        <p style={{ color: '#6b7280', marginBottom: '1rem' }}>
          You need to create at least one identity type before you can create identities.
        </p>
        <button 
          onClick={() => navigate('/types/new')}
          style={{ 
            background: '#2563eb', 
            color: 'white', 
            border: 'none', 
            padding: '10px 16px', 
            borderRadius: 6,
            cursor: 'pointer'
          }}
        >
          Create Identity Type
        </button>
      </div>
    )
  }

  return (
    <form onSubmit={onSubmit} style={{ display: 'grid', gap: 16, maxWidth: 500 }}>
             <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
         <h2>{mode === 'create' ? 'New Identity' : 'Edit Identity'}</h2>
       </div>
      
      {error && (
        <div style={{ color: '#b91c1c', padding: '12px', background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 6 }}>
          <div style={{ fontWeight: 500, marginBottom: '8px' }}>Error: {error}</div>
          {errorDetails && (
            <details style={{ marginTop: '8px' }}>
              <summary style={{ cursor: 'pointer', fontWeight: 500, color: '#7f1d1d' }}>
                Show Error Details
              </summary>
              <pre style={{ 
                marginTop: '8px', 
                padding: '8px', 
                background: '#f3f4f6', 
                border: '1px solid #d1d5db', 
                borderRadius: '4px', 
                fontSize: '0.75em', 
                overflow: 'auto',
                whiteSpace: 'pre-wrap'
              }}>
                {JSON.stringify(errorDetails, null, 2)}
              </pre>
            </details>
          )}
        </div>
      )}
      
      {mode === 'create' && (
        <label style={{ display: 'grid', gap: 4 }}>
          <span style={{ fontWeight: 500 }}>Identity Type *</span>
          <select 
            value={selectedIdentityType} 
            onChange={(e) => setSelectedIdentityType(e.target.value)}
            required
            style={{ padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 6, fontSize: '14px' }}
          >
            <option value="">Select an identity type</option>
            {identityTypes.map(type => (
              <option key={type.id} value={type.id}>
                {type.displayName}
              </option>
            ))}
          </select>
        </label>
      )}

      {selectedIdentityType && (
        <>
          <div style={{ display: 'grid', gap: 16 }}>
            <h3 style={{ margin: 0, fontSize: '1.1em', color: '#374151' }}>Basic Information</h3>
            
            <label style={{ display: 'grid', gap: 4 }}>
              <span style={{ fontWeight: 500 }}>Username *</span>
              <input 
                value={username} 
                onChange={(e) => setUsername(e.target.value)} 
                required 
                maxLength={100}
                style={{ padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 6, fontSize: '14px' }}
              />
            </label>
            
            <label style={{ display: 'grid', gap: 4 }}>
              <span style={{ fontWeight: 500 }}>Email *</span>
              <input 
                type="email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                required 
                maxLength={320}
                style={{ padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 6, fontSize: '14px' }}
              />
            </label>
            
            <label style={{ display: 'grid', gap: 4 }}>
              <span style={{ fontWeight: 500 }}>First name</span>
              <input 
                value={firstName} 
                onChange={(e) => setFirstName(e.target.value)} 
                maxLength={100}
                style={{ padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 6, fontSize: '14px' }}
              />
            </label>
            
            <label style={{ display: 'grid', gap: 4 }}>
              <span style={{ fontWeight: 500 }}>Last name</span>
              <input 
                value={lastName} 
                onChange={(e) => setLastName(e.target.value)} 
                maxLength={100}
                style={{ padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 6, fontSize: '14px' }}
              />
            </label>
          </div>

          {/* Dynamic attribute fields */}
          {attributes.length > 0 && (
            <div style={{ borderTop: '1px solid #e5e7eb', paddingTop: 20 }}>
              <h3 style={{ margin: '0 0 16px 0', fontSize: '1.1em', color: '#374151' }}>Attributes</h3>
              <div style={{ display: 'grid', gap: 16 }}>
                {attributes
                  .sort((a, b) => a.sortOrder - b.sortOrder)
                  .map(attribute => (
                    <label key={attribute.id} style={{ display: 'grid', gap: 4 }}>
                      <span style={{ fontWeight: 500 }}>
                        {attribute.displayName}
                        {attribute.required && <span style={{ color: '#dc2626' }}> *</span>}
                      </span>
                      {attribute.description && (
                        <div style={{ fontSize: '0.875em', color: '#6b7280', marginBottom: '4px' }}>
                          {attribute.description}
                        </div>
                      )}
                      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        {renderAttributeField(attribute)}
                      </div>
                    </label>
                  ))}
              </div>
            </div>
          )}
        </>
      )}

             <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
         <button 
           type="submit" 
           disabled={loading || (mode === 'create' && !selectedIdentityType)}
           style={{ 
             background: '#2563eb', 
             color: 'white', 
             border: 'none', 
             padding: '10px 16px', 
             borderRadius: 6,
             fontSize: '14px',
             fontWeight: 500,
             cursor: (loading || (mode === 'create' && !selectedIdentityType)) ? 'not-allowed' : 'pointer',
             opacity: (loading || (mode === 'create' && !selectedIdentityType)) ? 0.6 : 1
           }}
         >
           {loading ? 'Saving...' : (mode === 'create' ? 'Create' : 'Save')}
         </button>
         <button 
           type="button" 
           onClick={() => navigate('/')} 
           style={{ 
             background: '#6b7280', 
             color: 'white', 
             border: 'none', 
             padding: '10px 16px', 
             borderRadius: 6,
             fontSize: '14px',
             fontWeight: 500,
             cursor: 'pointer'
           }}
         >
           Cancel
         </button>
       </div>


    </form>
  )
}




