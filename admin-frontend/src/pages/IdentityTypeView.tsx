import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { IdentityType, getIdentityType } from '../api/client'

export default function IdentityTypeView() {
  const { name } = useParams<{ name: string }>()
  const [type, setType] = useState<IdentityType | null>(null)
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

  if (loading) return <div>Loading identity type...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>
  if (!type) return <div>Identity type not found</div>

  const activeAttributes = type.attributes.filter(attr => attr.active).sort((a, b) => a.sortOrder - b.sortOrder)
  const inactiveAttributes = type.attributes.filter(attr => !attr.active).sort((a, b) => a.sortOrder - b.sortOrder)

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>Identity Type: {type.displayName}</h2>
        <div style={{ display: 'flex', gap: '8px' }}>
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
            Edit
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
        <h3>Attributes ({activeAttributes.length} active, {inactiveAttributes.length} inactive)</h3>
        
        {activeAttributes.length > 0 && (
          <div style={{ marginBottom: '2rem' }}>
            <h4 style={{ color: '#059669', marginBottom: '1rem' }}>Active Attributes</h4>
            <div style={{ overflowX: 'auto' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
                <thead>
                  <tr style={{ background: '#f0fdf4' }}>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Name</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Display Name</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Type</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Required</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Default Value</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Order</th>
                  </tr>
                </thead>
                <tbody>
                  {activeAttributes.map((attribute) => (
                    <tr key={attribute.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                        {attribute.name}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.displayName}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        <span style={{ 
                          padding: '2px 8px', 
                          borderRadius: '12px', 
                          fontSize: '0.75rem',
                          background: '#dbeafe',
                          color: '#1e40af'
                        }}>
                          {attribute.dataType.toLowerCase()}
                        </span>
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.required ? (
                          <span style={{ color: '#dc2626', fontWeight: '500' }}>Yes</span>
                        ) : (
                          <span style={{ color: '#6b7280' }}>No</span>
                        )}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.defaultValue || '-'}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.sortOrder}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {inactiveAttributes.length > 0 && (
          <div>
            <h4 style={{ color: '#dc2626', marginBottom: '1rem' }}>Inactive Attributes</h4>
            <div style={{ overflowX: 'auto' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
                <thead>
                  <tr style={{ background: '#fef2f2' }}>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Name</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Display Name</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Type</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Required</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Default Value</th>
                    <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Order</th>
                  </tr>
                </thead>
                <tbody>
                  {inactiveAttributes.map((attribute) => (
                    <tr key={attribute.id} style={{ borderBottom: '1px solid #e5e7eb', opacity: 0.6 }}>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                        {attribute.name}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.displayName}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        <span style={{ 
                          padding: '2px 8px', 
                          borderRadius: '12px', 
                          fontSize: '0.75rem',
                          background: '#f3f4f6',
                          color: '#6b7280'
                        }}>
                          {attribute.dataType.toLowerCase()}
                        </span>
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.required ? (
                          <span style={{ color: '#dc2626', fontWeight: '500' }}>Yes</span>
                        ) : (
                          <span style={{ color: '#6b7280' }}>No</span>
                        )}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.defaultValue || '-'}
                      </td>
                      <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                        {attribute.sortOrder}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {type.attributes.length === 0 && (
          <div style={{ textAlign: 'center', padding: '2rem', color: '#6b7280', border: '2px dashed #e5e7eb', borderRadius: 8 }}>
            No attributes defined for this identity type.
          </div>
        )}
      </div>
    </div>
  )
}
