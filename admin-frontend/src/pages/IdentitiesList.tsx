import { useEffect, useState } from 'react'
import { deleteIdentity, listIdentities, type Identity, type Page } from '../api/client'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'

export default function IdentitiesList() {
  const navigate = useNavigate()
  const [params, setParams] = useSearchParams()
  const [page, setPage] = useState<Page<Identity> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const pageNum = Number(params.get('page') ?? '0')
  const size = Number(params.get('size') ?? '10')

  useEffect(() => {
    setLoading(true)
    listIdentities(pageNum, size)
      .then(setPage)
      .catch((e) => setError(String(e)))
      .finally(() => setLoading(false))
  }, [pageNum, size])

  const gotoPage = (n: number) => setParams({ page: String(n), size: String(size) })

  const onDelete = async (id: string) => {
    if (!confirm('Delete this identity?')) return
    await deleteIdentity(id)
    listIdentities(pageNum, size).then(setPage)
  }

  if (loading) return <div>Loading...</div>
  if (error) return <div style={{ color: '#b91c1c' }}>{error}</div>
  if (!page) return null

  return (
    <div>
      <div style={{ marginBottom: 12 }}>
        <button onClick={() => navigate('/new')} style={{ background: '#2563eb', color: 'white', border: 'none', padding: '6px 10px', borderRadius: 6 }}>New Identity</button>
      </div>
      <table style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr>
            <th style={th}>Username</th>
            <th style={th}>Email</th>
            <th style={th}>First</th>
            <th style={th}>Last</th>
            <th style={th}>Created</th>
            <th style={th}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {page.content.map((i) => (
            <tr key={i.id}>
              <td style={td}>{i.username}</td>
              <td style={td}>{i.email}</td>
              <td style={td}>{i.firstName ?? ''}</td>
              <td style={td}>{i.lastName ?? ''}</td>
              <td style={td}>{new Date(i.createdAt).toLocaleString()}</td>
              <td style={td}>
                <Link to={`/${i.id}/edit`} style={{ marginRight: 8 }}>Edit</Link>
                <button onClick={() => onDelete(i.id)} style={{ background: '#b91c1c', color: 'white', border: 'none', padding: '4px 8px', borderRadius: 6 }}>Delete</button>
              </td>
            </tr>
          ))}
          {page.content.length === 0 && (
            <tr><td style={td} colSpan={6}>No identities found.</td></tr>
          )}
        </tbody>
      </table>
      <div style={{ marginTop: 12, display: 'flex', gap: 8, alignItems: 'center' }}>
        <button disabled={page.number <= 0} onClick={() => gotoPage(page.number - 1)}>Prev</button>
        <span>Page {page.number + 1} of {page.totalPages}</span>
        <button disabled={page.number + 1 >= page.totalPages} onClick={() => gotoPage(page.number + 1)}>Next</button>
      </div>
    </div>
  )
}

const th: React.CSSProperties = { textAlign: 'left', borderBottom: '1px solid #ddd', padding: 8, background: '#f3f4f6' }
const td: React.CSSProperties = { borderBottom: '1px solid #eee', padding: 8 }


