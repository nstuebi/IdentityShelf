import { Link, Route, Routes, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import IdentitiesList from './pages/IdentitiesList'
import IdentityForm from './pages/IdentityForm'
import IdentityTypeList from './pages/IdentityTypeList'
import IdentityTypeForm from './pages/IdentityTypeForm'
import IdentityTypeView from './pages/IdentityTypeView'

export default function App() {
  return (
    <div style={{ fontFamily: 'system-ui, sans-serif', margin: '1.5rem' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1 style={{ margin: 0 }}>IdentityShelf Admin</h1>
        <nav style={{ display: 'flex', gap: 12 }}>
          <Link to="/">Identities</Link>
          <Link to="/types">Types</Link>
          <NewButton />
        </nav>
      </header>
      <main style={{ marginTop: '1rem' }}>
        <Routes>
          {/* Identity routes */}
          <Route path="/" element={<IdentitiesList />} />
          <Route path="/new" element={<IdentityForm mode="create" />} />
          <Route path=":id/edit" element={<IdentityForm mode="edit" />} />
          
          {/* Identity Type routes */}
          <Route path="/types" element={<IdentityTypeList />} />
          <Route path="/types/new" element={<IdentityTypeForm />} />
          <Route path="/types/:name/edit" element={<IdentityTypeForm />} />
          <Route path="/types/:name/view" element={<IdentityTypeView />} />
        </Routes>
      </main>
    </div>
  )
}

function NewButton() {
  const navigate = useNavigate()
  const [showDropdown, setShowDropdown] = useState(false)
  
  return (
    <div style={{ position: 'relative' }}>
      <button 
        onClick={() => setShowDropdown(!showDropdown)}
        style={{ 
          background: '#2563eb', 
          color: 'white', 
          border: 'none', 
          padding: '6px 10px', 
          borderRadius: 6,
          cursor: 'pointer'
        }}
      >
        New +
      </button>
      
      {showDropdown && (
        <div style={{
          position: 'absolute',
          top: '100%',
          right: 0,
          background: 'white',
          border: '1px solid #e5e7eb',
          borderRadius: 6,
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
          minWidth: '150px',
          zIndex: 1000
        }}>
          <button
            onClick={() => {
              navigate('/new')
              setShowDropdown(false)
            }}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: 'none',
              background: 'none',
              textAlign: 'left',
              cursor: 'pointer',
              borderBottom: '1px solid #f3f4f6'
            }}
            onMouseEnter={(e) => e.currentTarget.style.background = '#f9fafb'}
            onMouseLeave={(e) => e.currentTarget.style.background = 'none'}
          >
            New Identity
          </button>
          <button
            onClick={() => {
              navigate('/types/new')
              setShowDropdown(false)
            }}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: 'none',
              background: 'none',
              textAlign: 'left',
              cursor: 'pointer'
            }}
            onMouseEnter={(e) => e.currentTarget.style.background = '#f9fafb'}
            onMouseLeave={(e) => e.currentTarget.style.background = 'none'}
          >
            New Identity Type
          </button>
        </div>
      )}
      
      {/* Click outside to close dropdown */}
      {showDropdown && (
        <div 
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            zIndex: 999
          }}
          onClick={() => setShowDropdown(false)}
        />
      )}
    </div>
  )
}


