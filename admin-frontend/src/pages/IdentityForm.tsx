import { FormEvent, useEffect, useState } from 'react'
import { createIdentity, getIdentity, updateIdentity, listIdentityTypes, getIdentityType, getMappingsForIdentityType, IdentityType, IdentityTypeAttributeMapping } from '../api/client'
import { useNavigate, useParams } from 'react-router-dom'
import { Form, Button, Alert, Card, Row, Col, Spinner } from 'react-bootstrap'

export default function IdentityForm({ mode }: { mode: 'create' | 'edit' }) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [selectedIdentityType, setSelectedIdentityType] = useState<string>('')
  const [identityTypes, setIdentityTypes] = useState<IdentityType[]>([])
  const [attributeMappings, setAttributeMappings] = useState<IdentityTypeAttributeMapping[]>([])
  const [attributeValues, setAttributeValues] = useState<Record<string, any>>({})
  const [displayName, setDisplayName] = useState<string>('')
  const [status, setStatus] = useState<string>('ACTIVE')
  const [error, setError] = useState<string | null>(null)
  const [errorDetails, setErrorDetails] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    // Load identity types for create mode
    if (mode === 'create') {
      listIdentityTypes()
        .then(setIdentityTypes)
        .catch((e) => setError(String(e)))
    }
  }, [mode])

  useEffect(() => {
    if (mode === 'edit' && id) {
      getIdentity(id).then(async (identity) => {
        try {
          // Load the identity type to get its details
          const identityType = await getIdentityType(identity.identityType)
          setIdentityTypes([identityType])
          setSelectedIdentityType(identityType.id)
          
          // Load attribute mappings for this identity type
          const mappings = await getMappingsForIdentityType(identityType.id)
          setAttributeMappings(mappings)
          
          // Populate the form with existing attribute values
          setAttributeValues(identity.attributes)
          
          // Set the direct fields
          setDisplayName(identity.displayName)
          setStatus(identity.status)
        } catch (e) {
          setError(String(e))
        }
      }).catch((e) => setError(String(e)))
    }
  }, [mode, id])

  // When identity type changes in create mode, load its attributes and initialize defaults
  useEffect(() => {
    if (mode === 'create' && selectedIdentityType) {
      getMappingsForIdentityType(selectedIdentityType)
        .then((mappings) => {
          setAttributeMappings(mappings)
          // Initialize attribute values with defaults
          const initialValues: Record<string, any> = {}
          mappings.forEach((mapping) => {
            const defaultValue = mapping.effectiveDefaultValue || mapping.overrideDefaultValue || mapping.baseDefaultValue
            if (defaultValue) {
              initialValues[mapping.attributeTypeName] = defaultValue
            } else {
              initialValues[mapping.attributeTypeName] = ''
            }
          })
          setAttributeValues(initialValues)
        })
        .catch((e) => setError(String(e)))
    } else if (mode === 'create' && !selectedIdentityType) {
      setAttributeMappings([])
      setAttributeValues({})
    }
  }, [selectedIdentityType, mode])

  const handleAttributeChange = (attributeName: string, value: any) => {
    setAttributeValues((prev: Record<string, any>) => ({
      ...prev,
      [attributeName]: value
    }))
  }

  const renderAttributeField = (mapping: IdentityTypeAttributeMapping) => {
    const value = attributeValues[mapping.attributeTypeName] || ''
    const validationRegex = mapping.effectiveValidationRegex || mapping.overrideValidationRegex || mapping.baseValidationRegex
    
    switch (mapping.attributeDataType) {
      case 'BOOLEAN':
        return (
          <Form.Check
            type="checkbox"
            checked={value === true || value === 'true'}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.checked)}
            required={mapping.required}
          />
        )
      case 'INTEGER':
      case 'DECIMAL':
        return (
          <Form.Control
            type="number"
            value={value}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.value)}
            required={mapping.required}
            step={mapping.attributeDataType === 'DECIMAL' ? '0.01' : '1'}
          />
        )
      case 'DATE':
        return (
          <Form.Control
            type="date"
            value={value}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.value)}
            required={mapping.required}
          />
        )
      case 'DATETIME':
        return (
          <Form.Control
            type="datetime-local"
            value={value}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.value)}
            required={mapping.required}
          />
        )
      case 'EMAIL':
        return (
          <Form.Control
            type="email"
            value={value}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.value)}
            required={mapping.required}
          />
        )
      default: // STRING, PHONE, URL, SELECT, MULTI_SELECT
        return (
          <Form.Control
            type="text"
            value={value}
            onChange={(e) => handleAttributeChange(mapping.attributeTypeName, e.target.value)}
            required={mapping.required}
            placeholder={validationRegex ? 'Format: ' + validationRegex : undefined}
          />
        )
    }
  }

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    
    // Validate required fields
    if (mode === 'create' && !selectedIdentityType) {
      setError('Please select an identity type')
      setLoading(false)
      return
    }
    
    // Validate required attributes
    const missingRequiredAttributes = attributeMappings
      .filter(mapping => mapping.required)
      .filter(mapping => {
        const value = attributeValues[mapping.attributeTypeName]
        return value === undefined || value === null || value === ''
      })
    
    if (missingRequiredAttributes.length > 0) {
      setError(`Please fill in all required attributes: ${missingRequiredAttributes.map(mapping => mapping.attributeTypeDisplayName).join(', ')}`)
      setLoading(false)
      return
    }
    
    try {
      if (mode === 'create') {
        // Combine direct fields with dynamic attributes
        const allAttributes = {
          ...attributeValues,
          display_name: displayName,
          status: status
        }
        
        await createIdentity({ 
          identityType: selectedIdentityType,
          attributes: allAttributes
        })
      } else if (id) {
        // For edit mode, send all the current attribute values plus the direct fields
        const allAttributes = {
          ...attributeValues,
          display_name: displayName,
          status: status
        }
        
        await updateIdentity(id, {
          displayName: displayName,
          status: status,
          attributes: allAttributes
        })
      }
      navigate('/')
    } catch (e) {
      setError(String(e))
      // Capture detailed error information
      if (e instanceof Error) {
        const errorDetails: any = {
          message: e.message,
          name: e.name,
          stack: e.stack
        }
        
        // Check if the error has additional details from the API
        if ((e as any).details) {
          errorDetails.apiResponse = (e as any).details
        }
        
        setErrorDetails(errorDetails)
      } else if (typeof e === 'object' && e !== null) {
        setErrorDetails(e)
      } else {
        setErrorDetails({ raw: e })
      }
    } finally {
      setLoading(false)
    }
  }

  if (mode === 'create' && identityTypes.length === 0) {
    return (
      <div className="text-center my-4">
        <Spinner animation="border" className="me-2" />
        Loading identity types...
      </div>
    )
  }

  if (mode === 'create' && identityTypes.length === 0 && !loading) {
    return (
      <Card className="text-center p-4">
        <Card.Body>
          <Card.Title>No Identity Types Available</Card.Title>
          <Card.Text className="text-muted mb-3">
            You need to create at least one identity type before you can create identities.
          </Card.Text>
          <Button variant="primary" onClick={() => navigate('/types/new')}>
            Create Identity Type
          </Button>
        </Card.Body>
      </Card>
    )
  }

  return (
    <>
      <Row className="mb-3">
        <Col>
          <h2>{mode === 'create' ? 'New Identity' : 'Edit Identity'}</h2>
        </Col>
      </Row>
      
      {error && (
        <Alert variant="danger" className="mb-3">
          <Alert.Heading>Error: {error}</Alert.Heading>
          {errorDetails && (
            <details className="mt-2">
              <summary style={{ cursor: 'pointer' }}>
                Show Error Details
              </summary>
              <div className="mt-2">
                {errorDetails.apiResponse && (
                  <div className="mb-3">
                    <h6>API Response:</h6>
                    <pre className="bg-light p-2 border rounded" style={{ fontSize: '0.75em', overflow: 'auto' }}>
                      {JSON.stringify(errorDetails.apiResponse, null, 2)}
                    </pre>
                  </div>
                )}
                <h6>Full Error Details:</h6>
                <pre className="bg-light p-2 border rounded" style={{ fontSize: '0.75em', overflow: 'auto' }}>
                  {JSON.stringify(errorDetails, null, 2)}
                </pre>
              </div>
            </details>
          )}
        </Alert>
      )}
      
      <Form onSubmit={onSubmit} style={{ maxWidth: '600px' }}>
        {mode === 'create' && (
          <Form.Group className="mb-3">
            <Form.Label>Identity Type *</Form.Label>
            <Form.Select
              value={selectedIdentityType} 
              onChange={(e) => setSelectedIdentityType(e.target.value)}
              required
            >
              <option value="">Select an identity type</option>
              {identityTypes.map(type => (
                <option key={type.id} value={type.id}>
                  {type.displayName}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        )}

        {selectedIdentityType && (
          <Card className="mb-4">
            <Card.Header>
              <Card.Title className="mb-0">Basic Information</Card.Title>
            </Card.Header>
            <Card.Body>
              <Form.Group className="mb-3">
                <Form.Label>Display Name *</Form.Label>
                <Form.Control
                  type="text"
                  value={displayName}
                  onChange={(e) => setDisplayName(e.target.value)}
                  required
                  placeholder="Enter display name"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Status *</Form.Label>
                <Form.Select
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                  required
                >
                  <option value="ACTIVE">Active</option>
                  <option value="SUSPENDED">Suspended</option>
                  <option value="ARCHIVED">Archived</option>
                  <option value="ESTABLISHED">Established</option>
                </Form.Select>
              </Form.Group>
            </Card.Body>
          </Card>
        )}

        {selectedIdentityType && attributeMappings.length > 0 && (
          <Card className="mb-4">
            <Card.Header>
              <Card.Title className="mb-0">Identity Attributes</Card.Title>
            </Card.Header>
            <Card.Body>
              {attributeMappings
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map(mapping => (
                  <Form.Group key={mapping.id} className="mb-3">
                    <Form.Label>
                      {mapping.attributeTypeDisplayName}
                      {mapping.required && <span className="text-danger"> *</span>}
                    </Form.Label>
                    {mapping.attributeTypeDescription && (
                      <Form.Text className="text-muted d-block mb-2">
                        {mapping.attributeTypeDescription}
                      </Form.Text>
                    )}
                    {renderAttributeField(mapping)}
                  </Form.Group>
                ))}
            </Card.Body>
          </Card>
        )}

        <div className="d-flex gap-2">
          <Button 
            type="submit" 
            variant="primary"
            disabled={loading || (mode === 'create' && !selectedIdentityType)}
          >
            {loading ? (
              <>
                <Spinner animation="border" size="sm" className="me-2" />
                Saving...
              </>
            ) : (
              mode === 'create' ? 'Create' : 'Save'
            )}
          </Button>
          <Button 
            type="button" 
            variant="secondary"
            onClick={() => navigate('/')}
          >
            Cancel
          </Button>
        </div>
      </Form>
    </>
  )
}




