import { useEffect, useState } from 'react'
import { deleteIdentity, listIdentities, type Identity, type Page } from '../api/client'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { Table, Button, Badge, Spinner, Alert, Row, Col } from 'react-bootstrap'

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

  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>
  if (error) return <Alert variant="danger">{error}</Alert>
  if (!page) return null

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h2>Identities</h2>
        </Col>
        <Col xs="auto">
          <Button variant="primary" onClick={() => navigate('/new')}>New Identity</Button>
        </Col>
      </Row>
      
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Display Name</th>
            <th>Identity Type</th>
            <th>Status</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {page.content.map((i) => (
            <tr key={i.id}>
              <td>{i.displayName}</td>
              <td>{i.identityType}</td>
              <td>
                <Badge bg={getStatusVariant(i.status)}>
                  {i.status}
                </Badge>
              </td>
              <td>{new Date(i.createdAt).toLocaleString()}</td>
              <td>
                <Button as={Link} to={`/${i.id}/edit`} variant="outline-primary" size="sm" className="me-2">
                  Edit
                </Button>
                <Button variant="outline-danger" size="sm" onClick={() => onDelete(i.id)}>
                  Delete
                </Button>
              </td>
            </tr>
          ))}
          {page.content.length === 0 && (
            <tr><td colSpan={5} className="text-center text-muted">No identities found.</td></tr>
          )}
        </tbody>
      </Table>
      
      <Row className="align-items-center">
        <Col>
          <Button variant="outline-secondary" disabled={page.number <= 0} onClick={() => gotoPage(page.number - 1)}>
            Previous
          </Button>
        </Col>
        <Col xs="auto">
          <span>Page {page.number + 1} of {page.totalPages}</span>
        </Col>
        <Col className="text-end">
          <Button variant="outline-secondary" disabled={page.number + 1 >= page.totalPages} onClick={() => gotoPage(page.number + 1)}>
            Next
          </Button>
        </Col>
      </Row>
    </>
  )
}

function getStatusVariant(status: string): string {
  switch (status) {
    case 'ACTIVE':
      return 'success'
    case 'SUSPENDED':
      return 'danger'
    case 'ARCHIVED':
      return 'secondary'
    case 'ESTABLISHED':
      return 'primary'
    default:
      return 'secondary'
  }
}


