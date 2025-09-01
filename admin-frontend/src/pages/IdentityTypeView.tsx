import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { IdentityType, IdentityTypeAttributeMapping, getIdentityType } from '../api/client'
import AttributeMappingManager from '../components/AttributeMappingManager'

export default function IdentityTypeView() {
  const { name } = useParams<{ name: string }>()
  const [type, setType] = useState<IdentityType | null>(null)
  const [mappings, setMappings] = useState<IdentityTypeAttributeMapping[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (name) {
      loadIdentityType()
    }
  }, [name])

  async function loadIdentityType() {
    try {
      setLoading(true)
      const data = await getIdentityType(name!)
      setType(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load identity type')
    } finally {
      setLoading(false)
    }
  }

  const handleMappingsChange = (newMappings: IdentityTypeAttributeMapping[]) => {
    setMappings(newMappings)
  }

  if (loading) return <div>Loading identity type...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>
  if (!type) return <div>Identity type not found</div>



  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>Identity Type: {type.displayName}</h2>
        <div style={{ display: 'flex', gap: '8px' }}>
          <Link
            to={`/types/${type.name}/attributes/add`}
            style={{
              background: '#059669',
              color: 'white',
              textDecoration: 'none',
              padding: '8px 16px',
              borderRadius: 6
            }}
          >
            Add Attribute
          </Link>
          <Link
            to={`/types/${type.name}/edit`}
            style={{
              background: '#2563eb',
              color: 'white',
              textDecoration: 'none',
              padding: '8px 16px',
              borderRadius: 6
            }}
          >
            Edit Type
          </Link>
          <Link
            to="/types"
            style={{
              background: '#6b7280',
              color: 'white',
              textDecoration: 'none',
              padding: '8px 16px',
              borderRadius: 6
            }}
          >
            Back to Types
          </Link>
        </div>
      </div>

      <div style={{ 
        border: '1px solid #e5e7eb', 
        borderRadius: 8, 
        padding: '1.5rem', 
        marginBottom: '1.5rem',
        background: 'white'
      }}>
        <h3>Basic Information</h3>
        
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
          <div>
            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Name
              </label>
              <div style={{ fontSize: '1.125rem', fontWeight: '500' }}>{type.name}</div>
            </div>

            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Display Name
              </label>
              <div style={{ fontSize: '1.125rem' }}>{type.displayName}</div>
            </div>

            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Status
              </label>
              <span style={{ 
                padding: '4px 12px', 
                borderRadius: '20px', 
                fontSize: '0.875rem',
                background: type.active ? '#dcfce7' : '#fef2f2',
                color: type.active ? '#166534' : '#dc2626'
              }}>
                {type.active ? 'Active' : 'Inactive'}
              </span>
            </div>
          </div>

          <div>
            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Description
              </label>
              <div style={{ fontSize: '1rem', color: type.description ? '#374151' : '#9ca3af' }}>
                {type.description || 'No description provided'}
              </div>
            </div>

            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Created
              </label>
              <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                {new Date(type.createdAt).toLocaleDateString()}
              </div>
            </div>

            <div style={{ marginBottom: '1rem' }}>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500', color: '#6b7280' }}>
                Last Updated
              </label>
              <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                {new Date(type.updatedAt).toLocaleDateString()}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div style={{ 
        border: '1px solid #e5e7eb', 
        borderRadius: 8, 
        padding: '1.5rem',
        background: 'white'
      }}>
        <AttributeMappingManager
          identityTypeId={type.id}
          identityTypeName={type.displayName}
          onMappingsChange={handleMappingsChange}
        />
      </div>
    </div>
  )
}
