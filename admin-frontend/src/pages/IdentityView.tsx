import { useEffect, useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { Card, Badge, Button, Spinner, Alert, Row, Col, Table } from 'react-bootstrap'
import { getIdentity, getIdentityType, getMappingsForIdentityType, getIdentityTypeIdentifierMappings, getIdentifiersForIdentity, type Identity, type IdentityType, type IdentityTypeAttributeMapping, type IdentityTypeIdentifierMapping, type IdentityIdentifier } from '../api/client'

export default function IdentityView() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [identity, setIdentity] = useState<Identity | null>(null)
  const [identityType, setIdentityType] = useState<IdentityType | null>(null)
  const [attributeMappings, setAttributeMappings] = useState<IdentityTypeAttributeMapping[]>([])
  const [identifierMappings, setIdentifierMappings] = useState<IdentityTypeIdentifierMapping[]>([])
  const [identifiers, setIdentifiers] = useState<IdentityIdentifier[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) {
      setError('No identity ID provided')
      setLoading(false)
      return
    }

    loadIdentity()
  }, [id])

  const loadIdentity = async () => {
    if (!id) return

    try {
      setLoading(true)
      
      // Load the identity
      const identityData = await getIdentity(id)
      setIdentity(identityData)

      // Load the identity type
      const identityTypeData = await getIdentityType(identityData.identityType)
      setIdentityType(identityTypeData)

      // Load attribute mappings and identifier mappings in parallel
      const [attributeMappings, identifierMappings, identifiers] = await Promise.all([
        getMappingsForIdentityType(identityTypeData.id),
        getIdentityTypeIdentifierMappings(identityTypeData.id),
        getIdentifiersForIdentity(identityData.id)
      ])
      
      setAttributeMappings(attributeMappings)
      setIdentifierMappings(identifierMappings)
      setIdentifiers(identifiers)

      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load identity')
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="text-center my-4">
        <Spinner animation="border" className="me-2" />
        Loading identity...
      </div>
    )
  }

  if (error) {
    return <Alert variant="danger">Error: {error}</Alert>
  }

  if (!identity || !identityType) {
    return <Alert variant="warning">Identity not found</Alert>
  }

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h2>View Identity</h2>
        </Col>
        <Col xs="auto">
          <Button as={Link} to={`/${identity.id}/edit`} variant="primary" className="me-2">
            Edit Identity
          </Button>
          <Button variant="secondary" onClick={() => navigate('/')}>
            Back to List
          </Button>
        </Col>
      </Row>

      {/* Basic Information Card */}
      <Card className="mb-4">
        <Card.Header>
          <Card.Title className="mb-0">Basic Information</Card.Title>
        </Card.Header>
        <Card.Body>
          <Table borderless>
            <tbody>
              <tr>
                <td className="fw-bold" style={{ width: '200px' }}>Display Name:</td>
                <td>{identity.displayName}</td>
              </tr>
              <tr>
                <td className="fw-bold">Identity Type:</td>
                <td>{identityType.displayName} ({identityType.name})</td>
              </tr>
              <tr>
                <td className="fw-bold">Status:</td>
                <td>
                  <Badge bg={getStatusVariant(identity.status)}>
                    {identity.status}
                  </Badge>
                </td>
              </tr>
              <tr>
                <td className="fw-bold">Created:</td>
                <td>{new Date(identity.createdAt).toLocaleString()}</td>
              </tr>
              <tr>
                <td className="fw-bold">Updated:</td>
                <td>{new Date(identity.updatedAt).toLocaleString()}</td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Identity Type Description */}
      {identityType.description && (
        <Card className="mb-4">
          <Card.Header>
            <Card.Title className="mb-0">Identity Type Description</Card.Title>
          </Card.Header>
          <Card.Body>
            <p className="mb-0">{identityType.description}</p>
          </Card.Body>
        </Card>
      )}

      {/* Identifiers Card */}
      {identifierMappings.length > 0 && (
        <Card className="mb-4">
          <Card.Header>
            <Card.Title className="mb-0">Identifiers</Card.Title>
          </Card.Header>
          <Card.Body>
            <Table borderless>
              <tbody>
                {identifierMappings
                  .filter(mapping => mapping.active)
                  .sort((a, b) => a.sortOrder - b.sortOrder)
                  .map(mapping => {
                    const identifier = identifiers.find(id => id.identifierTypeName === mapping.identifierTypeName)
                    return (
                      <tr key={mapping.id}>
                        <td className="fw-bold" style={{ width: '200px' }}>
                          {mapping.identifierTypeDisplayName}
                          {mapping.required && <span className="text-danger ms-1">*</span>}
                          {mapping.primaryCandidate && <span className="text-primary ms-1">(Primary)</span>}:
                        </td>
                        <td>
                          {identifier ? (
                            <div>
                              <span>{identifier.identifierValue}</span>
                              <div className="d-flex gap-2 mt-1">
                                {identifier.primary && (
                                  <Badge bg="primary" size="sm">Primary</Badge>
                                )}
                                {identifier.verified && (
                                  <Badge bg="success" size="sm">Verified</Badge>
                                )}
                                {!identifier.active && (
                                  <Badge bg="secondary" size="sm">Inactive</Badge>
                                )}
                              </div>
                              <div>
                                <small className="text-muted">
                                  Type: {mapping.identifierDataType.toLowerCase()}
                                  {mapping.identifierTypeDescription && (
                                    <> • {mapping.identifierTypeDescription}</>
                                  )}
                                  {identifier.verified && identifier.verifiedAt && (
                                    <> • Verified: {new Date(identifier.verifiedAt).toLocaleString()}</>
                                  )}
                                </small>
                              </div>
                            </div>
                          ) : (
                            <span className="text-muted fst-italic">No value</span>
                          )}
                        </td>
                      </tr>
                    )
                  })}
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      )}

      {/* Empty Identifiers State */}
      {identifierMappings.filter(mapping => mapping.active).length === 0 && (
        <Card className="mb-4">
          <Card.Header>
            <Card.Title className="mb-0">Identifiers</Card.Title>
          </Card.Header>
          <Card.Body className="text-center text-muted">
            <p className="mb-0">No identifiers defined for this identity type.</p>
          </Card.Body>
        </Card>
      )}

      {/* Attributes Card */}
      {attributeMappings.length > 0 && (
        <Card className="mb-4">
          <Card.Header>
            <Card.Title className="mb-0">Attributes</Card.Title>
          </Card.Header>
          <Card.Body>
            <Table borderless>
              <tbody>
                {attributeMappings
                  .filter(mapping => mapping.active)
                  .sort((a, b) => a.sortOrder - b.sortOrder)
                  .map(mapping => {
                    const value = identity.attributes?.[mapping.attributeTypeName]
                    return (
                      <tr key={mapping.id}>
                        <td className="fw-bold" style={{ width: '200px' }}>
                          {mapping.attributeTypeDisplayName}
                          {mapping.required && <span className="text-danger ms-1">*</span>}:
                        </td>
                        <td>
                          {value !== undefined && value !== null && value !== '' ? (
                            <span>{String(value)}</span>
                          ) : (
                            <span className="text-muted fst-italic">No value</span>
                          )}
                          <div>
                            <small className="text-muted">
                              Type: {mapping.attributeDataType.toLowerCase()}
                              {mapping.attributeTypeDescription && (
                                <> • {mapping.attributeTypeDescription}</>
                              )}
                            </small>
                          </div>
                        </td>
                      </tr>
                    )
                  })}
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      )}

      {/* Empty Attributes State */}
      {attributeMappings.filter(mapping => mapping.active).length === 0 && (
        <Card className="mb-4">
          <Card.Header>
            <Card.Title className="mb-0">Attributes</Card.Title>
          </Card.Header>
          <Card.Body className="text-center text-muted">
            <p className="mb-0">No attributes defined for this identity type.</p>
          </Card.Body>
        </Card>
      )}
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
