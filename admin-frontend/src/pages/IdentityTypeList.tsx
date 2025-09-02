import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Table, Button, Badge, Spinner, Alert, Row, Col, Card } from 'react-bootstrap'
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

  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>
  if (error) return <Alert variant="danger">Error: {error}</Alert>

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h2>Identity Types</h2>
        </Col>
        <Col xs="auto">
          <Button as={Link} to="/types/new" variant="primary">
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
                  {type.attributes.length === 0 ? (
                    <span className="text-muted fst-italic">No attributes</span>
                  ) : (
                    <div>
                      {type.attributes
                        .filter(attr => attr.active)
                        .sort((a, b) => a.sortOrder - b.sortOrder)
                        .map(attr => (
                          <div key={attr.id} className="mb-1">
                            <span className="fw-medium">{attr.displayName}</span>
                            <small className="text-muted ms-2">
                              ({attr.dataType.toLowerCase()}{attr.required ? ', required' : ''})
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
                  <Button as={Link} to={`/types/${type.name}/edit`} variant="outline-primary" size="sm" className="me-2">
                    Edit
                  </Button>
                  <Button as={Link} to={`/types/${type.name}/view`} variant="outline-success" size="sm">
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
