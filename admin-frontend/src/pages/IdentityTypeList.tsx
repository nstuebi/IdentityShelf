import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Table, Button, Badge, Spinner, Alert, Row, Col, Card } from 'react-bootstrap'
import { IdentityType, IdentityTypeIdentifierMapping, listIdentityTypes, getIdentityTypeIdentifierMappings } from '../api/client'

export default function IdentityTypeList() {
  const [types, setTypes] = useState<IdentityType[]>([])
  const [identifierMappings, setIdentifierMappings] = useState<Record<string, IdentityTypeIdentifierMapping[]>>({})
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
      
      // Load identifier mappings for each identity type
      const mappingsPromises = data.map(async (type) => {
        try {
          const mappings = await getIdentityTypeIdentifierMappings(type.id)
          return { typeId: type.id, mappings }
        } catch (err) {
          console.warn(`Failed to load identifier mappings for ${type.name}:`, err)
          return { typeId: type.id, mappings: [] }
        }
      })
      
      const mappingsResults = await Promise.all(mappingsPromises)
      const mappingsMap: Record<string, IdentityTypeIdentifierMapping[]> = {}
      mappingsResults.forEach(result => {
        mappingsMap[result.typeId] = result.mappings
      })
      setIdentifierMappings(mappingsMap)
      
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load types')
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>
  if (error) return <Alert variant="danger">Error: {error}</Alert>

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h2>Identity Types</h2>
        </Col>
        <Col xs="auto">
          <Button as={Link as any} to="/types/new" variant="primary">
            New Type
          </Button>
        </Col>
      </Row>

      {types.length === 0 ? (
        <Card className="text-center p-4">
          <Card.Body>
            <h5>No identity types found</h5>
            <p className="text-muted">Create your first one!</p>
          </Card.Body>
        </Card>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Name</th>
              <th>Display Name</th>
              <th>Description</th>
              <th>Attributes</th>
              <th>Identifiers</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {types.map((type) => (
              <tr key={type.id}>
                <td>
                  <strong>{type.name}</strong>
                </td>
                <td>
                  {type.displayName}
                </td>
                <td>
                  {type.description || <span className="text-muted">-</span>}
                </td>
                <td>
                  {!type.attributes || type.attributes.length === 0 ? (
                    <span className="text-muted fst-italic">No attributes</span>
                  ) : (
                    <div>
                      {type.attributes
                        .filter(attr => attr.active)
                        .map(attr => (
                          <div key={attr.id} className="mb-1">
                            <span className="fw-medium">{attr.displayName}</span>
                            <small className="text-muted ms-2">
                              ({attr.dataType.toLowerCase()})
                            </small>
                          </div>
                        ))
                      }
                    </div>
                  )}
                </td>
                <td>
                  {!identifierMappings[type.id] || identifierMappings[type.id].length === 0 ? (
                    <span className="text-muted fst-italic">No identifiers</span>
                  ) : (
                    <div>
                      {identifierMappings[type.id]
                        .filter(mapping => mapping.active)
                        .sort((a, b) => a.sortOrder - b.sortOrder)
                        .map(mapping => (
                          <div key={mapping.id} className="mb-1">
                            <span className="fw-medium">{mapping.identifierTypeDisplayName}</span>
                            <small className="text-muted ms-2">
                              ({mapping.identifierDataType.toLowerCase()}
                              {mapping.required ? ', required' : ''}
                              {mapping.primaryCandidate ? ', primary' : ''})
                            </small>
                          </div>
                        ))
                      }
                    </div>
                  )}
                </td>
                <td>
                  <Badge bg={type.active ? 'success' : 'secondary'}>
                    {type.active ? 'Active' : 'Inactive'}
                  </Badge>
                </td>
                <td>
                  <Button as={Link as any} to={`/types/${type.name}/edit`} variant="outline-primary" size="sm" className="me-2">
                    Edit
                  </Button>
                  <Button as={Link as any} to={`/types/${type.name}/view`} variant="outline-success" size="sm">
                    View
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </>
  )
}
