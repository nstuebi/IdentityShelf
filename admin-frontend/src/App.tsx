import { Link, Route, Routes, useNavigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import IdentitiesList from './pages/IdentitiesList'
import IdentityForm from './pages/IdentityForm'
import IdentityTypeList from './pages/IdentityTypeList'
import IdentityTypeForm from './pages/IdentityTypeForm'
import IdentityTypeView from './pages/IdentityTypeView'
import AttributeTypeList from './pages/AttributeTypeList'
import AttributeTypeForm from './pages/AttributeTypeForm'
import AttributeForm from './components/AttributeForm'
import IdentifierTypeList from './pages/IdentifierTypeList'
import IdentifierTypeForm from './pages/IdentifierTypeForm'
import IdentifierSearch from './pages/IdentifierSearch'
import { getBuildInfo, BuildInfo } from './api/client'

export default function App() {
  const [buildInfo, setBuildInfo] = useState<BuildInfo | null>(null)
  const [frontendBuildTime] = useState(() => new Date().toISOString())
  
  useEffect(() => {
    // Fetch build info from backend
    getBuildInfo()
      .then(setBuildInfo)
      .catch(() => setBuildInfo(null)) // Silently fail if backend is not available
  }, [])

  return (
    <div style={{ fontFamily: 'system-ui, sans-serif', margin: '1.5rem' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
          <h1 style={{ margin: 0 }}>IdentityShelf Admin</h1>
          <div style={{ fontSize: '0.75em', color: '#6b7280', marginTop: '4px' }}>
            <span>Frontend: {new Date(frontendBuildTime).toLocaleString()}</span>
            {buildInfo && (
              <>
                <span style={{ margin: '0 8px' }}>•</span>
                <span>Backend: {new Date(buildInfo.buildTime).toLocaleString()}</span>
              </>
            )}
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <nav style={{ display: 'flex', gap: 12 }}>
            <Link to="/">Identities</Link>
            <Link to="/types">Identity Types</Link>
            <Link to="/attribute-types">Attribute Types</Link>
            <Link to="/identifier-types">Identifier Types</Link>
            <Link to="/search">🔍 Search</Link>
            <NewButton />
          </nav>
        </div>
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
          
          {/* Attribute Type routes */}
          <Route path="/attribute-types" element={<AttributeTypeList />} />
          <Route path="/attribute-types/create" element={<AttributeTypeForm mode="create" />} />
          <Route path="/attribute-types/:id/edit" element={<AttributeTypeForm mode="edit" />} />
          
          {/* Legacy Attribute routes */}
          <Route path="/types/:typeName/attributes/add" element={<AttributeForm mode="create" />} />
          <Route path="/types/:typeName/attributes/:attributeId/edit" element={<AttributeForm mode="edit" />} />
          
          {/* Identifier Type routes */}
          <Route path="/identifier-types" element={<IdentifierTypeList />} />
          <Route path="/identifier-types/new" element={<IdentifierTypeForm />} />
          <Route path="/identifier-types/:id" element={<IdentifierTypeForm />} />
          <Route path="/identifier-types/:id/edit" element={<IdentifierTypeForm />} />
          
          {/* Identifier Search */}
          <Route path="/search" element={<IdentifierSearch />} />
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
              cursor: 'pointer',
              borderBottom: '1px solid #f3f4f6'
            }}
            onMouseEnter={(e) => e.currentTarget.style.background = '#f9fafb'}
            onMouseLeave={(e) => e.currentTarget.style.background = 'none'}
          >
            New Identity Type
          </button>
          <button
            onClick={() => {
              navigate('/attribute-types/create')
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
            New Attribute Type
          </button>
          <button
            onClick={() => {
              navigate('/identifier-types/new')
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
            New Identifier Type
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


