import { FormEvent, useEffect, useState } from 'react'
import { createIdentity, getIdentity, updateIdentity } from '../api/client'
import { useNavigate, useParams } from 'react-router-dom'

export default function IdentityForm({ mode }: { mode: 'create' | 'edit' }) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (mode === 'edit' && id) {
      getIdentity(id).then((i) => {
        setUsername(i.username)
        setEmail(i.email)
        setFirstName(i.firstName ?? '')
        setLastName(i.lastName ?? '')
      }).catch((e) => setError(String(e)))
    }
  }, [mode, id])

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    try {
      if (mode === 'create') {
        await createIdentity({ username, email, firstName, lastName })
      } else if (id) {
        await updateIdentity(id, { username, email, firstName, lastName })
      }
      navigate('/')
    } catch (e) {
      setError(String(e))
    }
  }

  return (
    <form onSubmit={onSubmit} style={{ display: 'grid', gap: 12, maxWidth: 420 }}>
      <h2>{mode === 'create' ? 'New Identity' : 'Edit Identity'}</h2>
      {error && <div style={{ color: '#b91c1c' }}>{error}</div>}
      <label>
        Username
        <input value={username} onChange={(e) => setUsername(e.target.value)} required maxLength={100} />
      </label>
      <label>
        Email
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required maxLength={320} />
      </label>
      <label>
        First name
        <input value={firstName} onChange={(e) => setFirstName(e.target.value)} maxLength={100} />
      </label>
      <label>
        Last name
        <input value={lastName} onChange={(e) => setLastName(e.target.value)} maxLength={100} />
      </label>
      <div style={{ display: 'flex', gap: 8 }}>
        <button type="submit" style={{ background: '#2563eb', color: 'white', border: 'none', padding: '6px 10px', borderRadius: 6 }}>{mode === 'create' ? 'Create' : 'Save'}</button>
        <button type="button" onClick={() => navigate('/')} style={{ background: '#4b5563', color: 'white', border: 'none', padding: '6px 10px', borderRadius: 6 }}>Cancel</button>
      </div>
    </form>
  )
}


