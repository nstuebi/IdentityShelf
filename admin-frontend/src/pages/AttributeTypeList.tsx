import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { listAttributeTypes, deleteAttributeType, AttributeType } from '../api/client'

export default function AttributeTypeList() {
  const navigate = useNavigate()
  const [attributeTypes, setAttributeTypes] = useState<AttributeType[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadAttributeTypes()
  }, [])

  const loadAttributeTypes = async () => {
    try {
      setLoading(true)
      const data = await listAttributeTypes()
      setAttributeTypes(data)
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id: string, name: string) => {
    if (confirm(`Are you sure you want to delete attribute type "${name}"?`)) {
      try {
        await deleteAttributeType(id)
        await loadAttributeTypes()
      } catch (err) {
        setError(String(err))
      }
    }
  }

  if (loading) return <div>Loading...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h1>Attribute Types</h1>
        <Link 
          to="/attribute-types/create"
          style={{
            background: '#059669',
            color: 'white',
            padding: '8px 16px',
            borderRadius: 6,
            textDecoration: 'none'
          }}
        >
          Create Attribute Type
        </Link>
      </div>

      <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0f9ff', borderRadius: 8 }}>
        <h3 style={{ margin: '0 0 0.5rem 0' }}>About Attribute Types</h3>
        <p style={{ margin: 0, fontSize: '0.875rem', color: '#374151' }}>
          Attribute types define reusable field definitions that can be assigned to multiple identity types. 
          Each attribute type defines the base validation rules and defaults, which can be overridden per identity type.
        </p>
      </div>

      {attributeTypes.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '3rem',
          border: '2px dashed #e5e7eb',
          borderRadius: 8,
          color: '#6b7280'
        }}>
          <h3>No attribute types found</h3>
          <p>Create your first attribute type to get started.</p>
        </div>
      ) : (
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #e5e7eb' }}>
            <thead>
              <tr style={{ background: '#f9fafb' }}>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Name</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Display Name</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Data Type</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Default Value</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Validation</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Status</th>
                <th style={{ padding: '12px', textAlign: 'left', border: '1px solid #e5e7eb' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {attributeTypes.map((attributeType) => (
                <tr key={attributeType.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb', fontWeight: '500' }}>
                    {attributeType.name}
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    {attributeType.displayName}
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <span style={{
                      background: '#f3f4f6',
                      padding: '2px 8px',
                      borderRadius: 4,
                      fontSize: '0.75rem',
                      textTransform: 'uppercase'
                    }}>
                      {attributeType.dataType}
                    </span>
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    {attributeType.defaultValue || 
                      <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                    }
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    {attributeType.validationRegex ? (
                      <code style={{ 
                        background: '#f3f4f6', 
                        padding: '2px 4px', 
                        borderRadius: 3,
                        fontSize: '0.75rem',
                        maxWidth: '200px',
                        display: 'inline-block',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        whiteSpace: 'nowrap'
                      }}>
                        {attributeType.validationRegex}
                      </code>
                    ) : (
                      <span style={{ color: '#6b7280', fontStyle: 'italic' }}>None</span>
                    )}
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <span style={{
                      background: attributeType.active ? '#dcfce7' : '#fef2f2',
                      color: attributeType.active ? '#166534' : '#dc2626',
                      padding: '4px 8px',
                      borderRadius: 4,
                      fontSize: '0.75rem',
                      fontWeight: '500'
                    }}>
                      {attributeType.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td style={{ padding: '12px', border: '1px solid #e5e7eb' }}>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <Link
                        to={`/attribute-types/${attributeType.id}/edit`}
                        style={{
                          color: '#2563eb',
                          textDecoration: 'none',
                          fontSize: '0.875rem'
                        }}
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDelete(attributeType.id, attributeType.name)}
                        style={{
                          background: 'none',
                          border: 'none',
                          color: '#dc2626',
                          cursor: 'pointer',
                          fontSize: '0.875rem'
                        }}
                      >
                        Delete
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
