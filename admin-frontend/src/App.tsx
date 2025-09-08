import { Link, Route, Routes, useNavigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import { Container, Navbar, Nav, NavDropdown, Button } from 'react-bootstrap'
import IdentitiesList from './pages/IdentitiesList'
import IdentityForm from './pages/IdentityForm'
import IdentityView from './pages/IdentityView'
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
    <>
      <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
        <Container>
          <Navbar.Brand as={Link} to="/">
            <div>
              <div>IdentityShelf Admin</div>
              <div style={{ fontSize: '0.75em', opacity: 0.8 }}>
                <span>Frontend: {new Date(frontendBuildTime).toLocaleString()}</span>
                {buildInfo && (
                  <>
                    <span style={{ margin: '0 8px' }}>•</span>
                    <span>Backend: {new Date(buildInfo.buildTime).toLocaleString()}</span>
                  </>
                )}
              </div>
            </div>
          </Navbar.Brand>
          
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to="/">Identities</Nav.Link>
              <Nav.Link as={Link} to="/types">Identity Types</Nav.Link>
              <Nav.Link as={Link} to="/attribute-types">Attribute Types</Nav.Link>
              <Nav.Link as={Link} to="/identifier-types">Identifier Types</Nav.Link>
              <Nav.Link as={Link} to="/search">🔍 Search</Nav.Link>
            </Nav>
            <NewButton />
          </Navbar.Collapse>
        </Container>
      </Navbar>
      
      <Container>
        <main>
        <Routes>
          {/* Identity routes */}
          <Route path="/" element={<IdentitiesList />} />
          <Route path="/new" element={<IdentityForm mode="create" />} />
          <Route path=":id/view" element={<IdentityView />} />
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
      </Container>
    </>
  )
}

function NewButton() {
  const navigate = useNavigate()
  
  return (
    <NavDropdown title="New +" id="basic-nav-dropdown" align="end">
      <NavDropdown.Item onClick={() => navigate('/new')}>
        New Identity
      </NavDropdown.Item>
      <NavDropdown.Item onClick={() => navigate('/types/new')}>
        New Identity Type
      </NavDropdown.Item>
      <NavDropdown.Item onClick={() => navigate('/attribute-types/create')}>
        New Attribute Type
      </NavDropdown.Item>
      <NavDropdown.Item onClick={() => navigate('/identifier-types/new')}>
        New Identifier Type
      </NavDropdown.Item>
    </NavDropdown>
  )
}


