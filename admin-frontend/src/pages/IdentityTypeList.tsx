import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { IdentityType, listIdentityTypes } from '../api/client'

export default function IdentityTypeList() {
  const [types, setTypes] = useState<IdentityType[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadTypes()
  }, [])

  async function loadTypes() {
    try {
      setLoading(true)
      const data = await listIdentityTypes()
      setTypes(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load types')
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div>Loading identity types...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>Identity Types</h2>
        <Link 
          to="/types/new" 
          style={{ 
            background: '#2563eb', 
            color: 'white', 
            textDecoration: 'none', 
            padding: '8px 16px', 
            borderRadius: 6 
          }}
        >
          New Type
        </Link>
      </div>

      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
          <thead>
            <tr style={{ background: '#f9fafb' }}>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Name</th>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Display Name</th>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Description</th>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Attributes</th>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Status</th>
              <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {types.map((type) => (
              <tr key={type.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb', fontWeight: 'bold' }}>
                  {type.name}
                </td>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                  {type.displayName}
                </td>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                  {type.description || '-'}
                </td>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
                    {type.attributes.length === 0 ? (
                      <span style={{ color: '#6b7280', fontStyle: 'italic' }}>No attributes</span>
                    ) : (
                      type.attributes
                        .filter(attr => attr.active)
                        .sort((a, b) => a.sortOrder - b.sortOrder)
                        .map(attr => (
                          <div key={attr.id} style={{ fontSize: '0.875rem' }}>
                            <span style={{ fontWeight: '500' }}>{attr.displayName}</span>
                            <span style={{ color: '#6b7280', marginLeft: '8px' }}>
                              ({attr.dataType.toLowerCase()}{attr.required ? ', required' : ''})
                            </span>
                          </div>
                        ))
                    )}
                  </div>
                </td>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                  <span style={{ 
                    padding: '4px 8px', 
                    borderRadius: '4px', 
                    fontSize: '0.875rem',
                    background: type.active ? '#dcfce7' : '#fef2f2',
                    color: type.active ? '#166534' : '#dc2626'
                  }}>
                    {type.active ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                  <Link 
                    to={`/types/${type.name}/edit`}
                    style={{ 
                      color: '#2563eb', 
                      textDecoration: 'none',
                      marginRight: '12px'
                    }}
                  >
                    Edit
                  </Link>
                  <Link 
                    to={`/types/${type.name}/view`}
                    style={{ 
                      color: '#059669', 
                      textDecoration: 'none'
                    }}
                  >
                    View
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {types.length === 0 && (
        <div style={{ textAlign: 'center', padding: '2rem', color: '#6b7280' }}>
          No identity types found. Create your first one!
        </div>
      )}
    </div>
  )
}
