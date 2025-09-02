export interface Identity {
  id: string
  username: string
  email: string
  displayName: string
  status: string
  identityType: string
  attributes: Record<string, any>
  createdAt: string
  updatedAt: string
}

export interface IdentityType {
  id: string
  name: string
  displayName: string
  description?: string
  active: boolean
  createdAt: string
  updatedAt: string
  attributes?: AttributeType[] // Legacy - will be phased out in favor of mappings
}

export interface AttributeType {
  id: string
  name: string
  displayName: string
  description?: string
  dataType: string
  defaultValue?: string
  validationRegex?: string
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface IdentityTypeAttributeMapping {
  id: string
  identityTypeId: string
  identityTypeName: string
  attributeTypeId: string
  attributeTypeName: string
  attributeTypeDisplayName: string
  attributeTypeDescription?: string
  attributeDataType: string
  sortOrder: number
  required: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
  active: boolean
  createdAt: string
  updatedAt: string
  effectiveValidationRegex?: string
  effectiveDefaultValue?: string
  baseValidationRegex?: string
  baseDefaultValue?: string
}

export interface IdentifierType {
  id: string
  name: string
  displayName: string
  description?: string
  dataType: string
  validationRegex?: string
  defaultValue?: string
  unique: boolean
  searchable: boolean
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface IdentityIdentifier {
  id: string
  identityId: string
  identifierTypeId: string
  identifierTypeName: string
  identifierTypeDisplayName: string
  identifierValue: string
  primary: boolean
  verified: boolean
  verifiedAt?: string
  verifiedBy?: string
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface IdentityTypeIdentifierMapping {
  id: string
  identityTypeId: string
  identityTypeName: string
  identifierTypeId: string
  identifierTypeName: string
  identifierTypeDisplayName: string
  identifierTypeDescription?: string
  identifierDataType: string
  sortOrder: number
  required: boolean
  primaryCandidate: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
  active: boolean
  createdAt: string
  updatedAt: string
  effectiveValidationRegex?: string
  effectiveDefaultValue?: string
  baseValidationRegex?: string
  baseDefaultValue?: string
}

export interface Page<T> {
  content: T[]
  number: number
  size: number
  totalPages: number
  totalElements: number
}

const headers = { 'Content-Type': 'application/json' }

// Identity API functions
export async function listIdentities(page = 0, size = 20): Promise<Page<Identity>> {
  const res = await fetch(`/api/identities?page=${page}&size=${size}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to list identities: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentity(id: string): Promise<Identity> {
  const res = await fetch(`/api/identities/${id}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Identity not found: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createIdentity(payload: {
  identityType: string
  attributes: Record<string, any>
}): Promise<Identity> {
  const res = await fetch('/api/identities', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    let errorMessage = `Failed to create identity: ${res.status} ${res.statusText}`
    let errorDetails: any = null
    
    try {
      const errorData = await res.json()
      errorDetails = errorData
      
      if (errorData.validationErrors && Array.isArray(errorData.validationErrors)) {
        // Handle validation errors
        const validationMessages = errorData.validationErrors
          .map((err: any) => `${err.field}: ${err.message}`)
          .join(', ')
        errorMessage = `Validation failed: ${validationMessages}`
      } else if (errorData.message) {
        errorMessage = errorData.message
      } else if (errorData.error) {
        errorMessage = `${errorData.error}: ${errorData.message || 'An error occurred'}`
      }
    } catch {
      // If we can't parse the error response, use the default message
    }
    
    const error = new Error(errorMessage)
    ;(error as any).details = errorDetails
    throw error
  }
  return res.json()
}

export async function updateIdentity(id: string, payload: Partial<Identity>): Promise<Identity> {
  const res = await fetch(`/api/identities/${id}`, { method: 'PUT', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update identity: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteIdentity(id: string): Promise<void> {
  const res = await fetch(`/api/identities/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete identity: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

// Identity Type API functions
export async function listIdentityTypes(): Promise<IdentityType[]> {
  const res = await fetch('/api/identity-types')
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to list identity types: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentityType(name: string): Promise<IdentityType> {
  const res = await fetch(`/api/identity-types/${name}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Identity type not found: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getAttributesForType(typeName: string): Promise<AttributeType[]> {
  const res = await fetch(`/api/identity-types/${typeName}/attributes`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get attributes for type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

// Attribute Type API functions (now independent)
export async function listAttributeTypes(): Promise<AttributeType[]> {
  const res = await fetch('/api/attribute-types')
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to list attribute types: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getAttributeType(id: string): Promise<AttributeType> {
  const res = await fetch(`/api/attribute-types/${id}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Attribute type not found: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createAttributeType(payload: {
  name: string
  displayName: string
  description?: string
  dataType: string
  defaultValue?: string
  validationRegex?: string
  active?: boolean
}): Promise<AttributeType> {
  const res = await fetch('/api/attribute-types', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to create attribute type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function updateAttributeType(id: string, payload: Partial<AttributeType>): Promise<AttributeType> {
  const res = await fetch(`/api/attribute-types/${id}`, { method: 'PUT', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update attribute type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteAttributeType(id: string): Promise<void> {
  const res = await fetch(`/api/attribute-types/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete attribute type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

// Identity Type Attribute Mapping API functions
export async function getMappingsForIdentityType(identityTypeId: string): Promise<IdentityTypeAttributeMapping[]> {
  const res = await fetch(`/api/identity-type-attribute-mappings/by-identity-type/${identityTypeId}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get mappings: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createMapping(payload: {
  identityTypeId: string
  attributeTypeId: string
  sortOrder?: number
  required?: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
}): Promise<IdentityTypeAttributeMapping> {
  const res = await fetch('/api/identity-type-attribute-mappings', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to create mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function updateMapping(id: string, payload: {
  sortOrder?: number
  required?: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
}): Promise<IdentityTypeAttributeMapping> {
  const res = await fetch(`/api/identity-type-attribute-mappings/${id}`, { method: 'PUT', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteMapping(id: string): Promise<void> {
  const res = await fetch(`/api/identity-type-attribute-mappings/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

// Identity Type Identifier Mapping API functions
export async function getIdentityTypeIdentifierMappings(identityTypeId: string): Promise<IdentityTypeIdentifierMapping[]> {
  const res = await fetch(`/api/identity-type-identifier-mappings/by-identity-type/${identityTypeId}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get identifier mappings: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createIdentityTypeIdentifierMapping(payload: {
  identityTypeId: string
  identifierTypeId: string
  sortOrder: number
  required: boolean
  primaryCandidate: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
}): Promise<IdentityTypeIdentifierMapping> {
  const res = await fetch('/api/identity-type-identifier-mappings', {
    method: 'POST',
    headers,
    body: JSON.stringify(payload)
  })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to create identifier mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function updateIdentityTypeIdentifierMapping(id: string, payload: {
  identityTypeId: string
  identifierTypeId: string
  sortOrder: number
  required: boolean
  primaryCandidate: boolean
  overrideValidationRegex?: string
  overrideDefaultValue?: string
}): Promise<IdentityTypeIdentifierMapping> {
  const res = await fetch(`/api/identity-type-identifier-mappings/${id}`, {
    method: 'PUT',
    headers,
    body: JSON.stringify(payload)
  })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update identifier mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteIdentityTypeIdentifierMapping(id: string): Promise<void> {
  const res = await fetch(`/api/identity-type-identifier-mappings/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete identifier mapping: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

// Build Info API functions
export interface BuildInfo {
  applicationName: string
  buildTime: string
  javaVersion: string
  osName: string
}

export async function getBuildInfo(): Promise<BuildInfo> {
  const res = await fetch('/api/build-info')
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get build info: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

// Identifier Type API functions
export async function listIdentifierTypes(): Promise<IdentifierType[]> {
  const res = await fetch('/api/identifier-types')
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to list identifier types: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getSearchableIdentifierTypes(): Promise<IdentifierType[]> {
  const res = await fetch('/api/identifier-types/searchable')
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to list searchable identifier types: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentifierType(id: string): Promise<IdentifierType> {
  const res = await fetch(`/api/identifier-types/${id}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Identifier type not found: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentifierTypeByName(name: string): Promise<IdentifierType> {
  const res = await fetch(`/api/identifier-types/by-name/${name}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Identifier type not found: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createIdentifierType(payload: {
  name: string
  displayName: string
  description?: string
  dataType: string
  validationRegex?: string
  defaultValue?: string
  unique?: boolean
  searchable?: boolean
}): Promise<IdentifierType> {
  const res = await fetch('/api/identifier-types', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to create identifier type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function updateIdentifierType(id: string, payload: {
  name: string
  displayName: string
  description?: string
  dataType: string
  validationRegex?: string
  defaultValue?: string
  unique?: boolean
  searchable?: boolean
}): Promise<IdentifierType> {
  const res = await fetch(`/api/identifier-types/${id}`, { method: 'PUT', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update identifier type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteIdentifierType(id: string): Promise<void> {
  const res = await fetch(`/api/identifier-types/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete identifier type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

export async function activateIdentifierType(id: string): Promise<void> {
  const res = await fetch(`/api/identifier-types/${id}/activate`, { method: 'POST' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to activate identifier type: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

// Identity Identifier API functions
export async function searchIdentifiersByValue(value: string): Promise<IdentityIdentifier[]> {
  const res = await fetch(`/api/identifiers/search?value=${encodeURIComponent(value)}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to search identifiers: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function searchIdentifiers(payload: {
  identifierTypeId?: string
  identifierValue?: string
  verified?: boolean
  primary?: boolean
}): Promise<IdentityIdentifier[]> {
  const res = await fetch('/api/identifiers/search', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to search identifiers: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentifierSuggestions(partial: string): Promise<IdentityIdentifier[]> {
  const res = await fetch(`/api/identifiers/search/suggestions?partial=${encodeURIComponent(partial)}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get identifier suggestions: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function findIdentifierByTypeAndValue(typeName: string, value: string): Promise<IdentityIdentifier | null> {
  const res = await fetch(`/api/identifiers/search/by-type-and-value?typeName=${encodeURIComponent(typeName)}&value=${encodeURIComponent(value)}`)
  if (res.status === 404) {
    return null
  }
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to find identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentifiersForIdentity(identityId: string): Promise<IdentityIdentifier[]> {
  const res = await fetch(`/api/identifiers/identity/${identityId}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get identifiers for identity: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getPrimaryIdentifierForIdentity(identityId: string): Promise<IdentityIdentifier | null> {
  const res = await fetch(`/api/identifiers/identity/${identityId}/primary`)
  if (res.status === 404) {
    return null
  }
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get primary identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getVerifiedIdentifiersForIdentity(identityId: string): Promise<IdentityIdentifier[]> {
  const res = await fetch(`/api/identifiers/identity/${identityId}/verified`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get verified identifiers: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function createIdentifier(payload: {
  identityId: string
  identifierTypeId: string
  identifierValue: string
  primary?: boolean
}): Promise<IdentityIdentifier> {
  const res = await fetch('/api/identifiers', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to create identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function updateIdentifier(id: string, identifierValue: string, primary = false): Promise<IdentityIdentifier> {
  const res = await fetch(`/api/identifiers/${id}?identifierValue=${encodeURIComponent(identifierValue)}&primary=${primary}`, { method: 'PUT' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to update identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function deleteIdentifier(id: string): Promise<void> {
  const res = await fetch(`/api/identifiers/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to delete identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
}

export async function verifyIdentifier(id: string, verifiedBy: string): Promise<IdentityIdentifier> {
  const res = await fetch(`/api/identifiers/${id}/verify?verifiedBy=${encodeURIComponent(verifiedBy)}`, { method: 'POST' })
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to verify identifier: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}

export async function getIdentifierCountByType(typeId: string): Promise<number> {
  const res = await fetch(`/api/identifiers/statistics/count-by-type/${typeId}`)
  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(`Failed to get identifier count: ${res.status} ${res.statusText}${errorText ? ` - ${errorText}` : ''}`)
  }
  return res.json()
}




