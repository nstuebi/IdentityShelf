import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { IdentifierType, listIdentifierTypes, deleteIdentifierType } from '../api/client'

export default function IdentifierTypeList() {
  const [identifierTypes, setIdentifierTypes] = useState<IdentifierType[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadIdentifierTypes()
  }, [])

  const loadIdentifierTypes = async () => {
    try {
      setLoading(true)
      const data = await listIdentifierTypes()
      setIdentifierTypes(data)
    } catch (err) {
      setError(String(err))
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id: string, name: string) => {
    if (confirm(`Are you sure you want to delete identifier type "${name}"?`)) {
      try {
        await deleteIdentifierType(id)
        await loadIdentifierTypes()
      } catch (err) {
        setError(String(err))
      }
    }
  }

  if (loading) return <div>Loading...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  return (
    <div className="container">
      <div className="flex justify-between items-center mb-4">
        <h1>🔧 Identifier Types (UPDATED)</h1>
        <Link to="/identifier-types/new" className="btn btn-success">
          Create Identifier Type
        </Link>
      </div>

      <div className="info-card">
        <h3>About Identifier Types</h3>
        <p>
          Identifier types define searchable identity markers like SSN, passport numbers, or driver licenses. 
          These are highly indexed for fast lookups and can enforce uniqueness and validation patterns.
        </p>
      </div>

      {identifierTypes.length === 0 ? (
        <div className="empty-state">
          <h3>No identifier types found</h3>
          <p>Create your first identifier type to get started.</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Display Name</th>
                <th>Data Type</th>
                <th>Default Value</th>
                <th>Validation</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {identifierTypes.map((identifierType) => (
                <tr key={identifierType.id}>
                  <td className="font-medium">
                    {identifierType.name}
                  </td>
                  <td>
                    {identifierType.displayName}
                  </td>
                  <td>
                    <span className="badge badge-gray text-xs" style={{ textTransform: 'uppercase' }}>
                      {identifierType.dataType}
                    </span>
                  </td>
                  <td>
                    {identifierType.defaultValue || 
                      <span className="text-gray-500" style={{ fontStyle: 'italic' }}>None</span>
                    }
                  </td>
                  <td>
                    {identifierType.validationRegex ? (
                      <code className="code" style={{ 
                        maxWidth: '200px',
                        display: 'inline-block',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        whiteSpace: 'nowrap'
                      }}>
                        {identifierType.validationRegex}
                      </code>
                    ) : (
                      <span className="text-gray-500" style={{ fontStyle: 'italic' }}>None</span>
                    )}
                  </td>
                  <td>
                    <span className={identifierType.active ? 'status-active' : 'status-inactive'}>
                      {identifierType.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    <div className="flex gap-2">
                      <Link
                        to={`/identifier-types/${identifierType.id}/edit`}
                        className="text-blue-600 text-sm"
                        style={{ textDecoration: 'none' }}
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDelete(identifierType.id, identifierType.name)}
                        className="text-red-600 text-sm"
                        style={{
                          background: 'none',
                          border: 'none',
                          cursor: 'pointer'
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
