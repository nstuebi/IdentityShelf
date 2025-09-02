import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Table, Button, Badge, Spinner, Alert, Row, Col, Card } from 'react-bootstrap'
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

  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>
  if (error) return <Alert variant="danger">Error: {error}</Alert>

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h1>Identifier Types</h1>
        </Col>
        <Col xs="auto">
          <Button as={Link} to="/identifier-types/new" variant="success">
            Create Identifier Type
          </Button>
        </Col>
      </Row>

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>About Identifier Types</Card.Title>
          <Card.Text>
            Identifier types define searchable identity markers like SSN, passport numbers, or driver licenses. 
            These are highly indexed for fast lookups and can enforce uniqueness and validation patterns.
          </Card.Text>
        </Card.Body>
      </Card>

      {identifierTypes.length === 0 ? (
        <Card className="text-center p-4">
          <Card.Body>
            <h5>No identifier types found</h5>
            <p className="text-muted">Create your first identifier type to get started.</p>
          </Card.Body>
        </Card>
      ) : (
        <Table striped bordered hover responsive>
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
                <td>
                  <strong>{identifierType.name}</strong>
                </td>
                <td>
                  {identifierType.displayName}
                </td>
                <td>
                  <Badge bg="light" text="dark" className="text-uppercase">
                    {identifierType.dataType}
                  </Badge>
                </td>
                <td>
                  {identifierType.defaultValue || 
                    <span className="text-muted fst-italic">None</span>
                  }
                </td>
                <td>
                  {identifierType.validationRegex ? (
                    <code className="text-break" style={{ fontSize: '0.8em', maxWidth: '200px', display: 'inline-block' }}>
                      {identifierType.validationRegex}
                    </code>
                  ) : (
                    <span className="text-muted fst-italic">None</span>
                  )}
                </td>
                <td>
                  <Badge bg={identifierType.active ? 'success' : 'secondary'}>
                    {identifierType.active ? 'Active' : 'Inactive'}
                  </Badge>
                </td>
                <td>
                  <Button as={Link} to={`/identifier-types/${identifierType.id}/edit`} variant="outline-primary" size="sm" className="me-2">
                    Edit
                  </Button>
                  <Button variant="outline-danger" size="sm" onClick={() => handleDelete(identifierType.id, identifierType.name)}>
                    Delete
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
