import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Table, Button, Badge, Spinner, Alert, Row, Col, Card } from 'react-bootstrap'
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

  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>
  if (error) return <Alert variant="danger">Error: {error}</Alert>

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h1>Attribute Types</h1>
        </Col>
        <Col xs="auto">
          <Button as={Link} to="/attribute-types/create" variant="success">
            Create Attribute Type
          </Button>
        </Col>
      </Row>

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>About Attribute Types</Card.Title>
          <Card.Text>
            Attribute types define reusable field definitions that can be assigned to multiple identity types. 
            Each attribute type defines the base validation rules and defaults, which can be overridden per identity type.
          </Card.Text>
        </Card.Body>
      </Card>

      {attributeTypes.length === 0 ? (
        <Card className="text-center p-4">
          <Card.Body>
            <h5>No attribute types found</h5>
            <p className="text-muted">Create your first attribute type to get started.</p>
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
            {attributeTypes.map((attributeType) => (
              <tr key={attributeType.id}>
                <td>
                  <strong>{attributeType.name}</strong>
                </td>
                <td>
                  {attributeType.displayName}
                </td>
                <td>
                  <Badge bg="light" text="dark" className="text-uppercase">
                    {attributeType.dataType}
                  </Badge>
                </td>
                <td>
                  {attributeType.defaultValue || 
                    <span className="text-muted fst-italic">None</span>
                  }
                </td>
                <td>
                  {attributeType.validationRegex ? (
                    <code className="text-break" style={{ fontSize: '0.8em', maxWidth: '200px', display: 'inline-block' }}>
                      {attributeType.validationRegex}
                    </code>
                  ) : (
                    <span className="text-muted fst-italic">None</span>
                  )}
                </td>
                <td>
                  <Badge bg={attributeType.active ? 'success' : 'secondary'}>
                    {attributeType.active ? 'Active' : 'Inactive'}
                  </Badge>
                </td>
                <td>
                  <Button as={Link} to={`/attribute-types/${attributeType.id}/edit`} variant="outline-primary" size="sm" className="me-2">
                    Edit
                  </Button>
                  <Button variant="outline-danger" size="sm" onClick={() => handleDelete(attributeType.id, attributeType.name)}>
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
