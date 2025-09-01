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
  attributes: AttributeType[]
}

export interface AttributeType {
  id: string
  name: string
  displayName: string
  description?: string
  dataType: string
  required: boolean
  defaultValue?: string
  validationRegex?: string
  sortOrder: number
  active: boolean
  createdAt: string
  updatedAt: string
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

// Attribute Type API functions
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
  required: boolean
  defaultValue?: string
  validationRegex?: string
  sortOrder: number
  active: boolean
  identityTypeId: string
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




