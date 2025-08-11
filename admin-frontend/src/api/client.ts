export interface Identity {
  id: string
  username: string
  email: string
  firstName?: string
  lastName?: string
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
  if (!res.ok) throw new Error('Failed to list identities')
  return res.json()
}

export async function getIdentity(id: string): Promise<Identity> {
  const res = await fetch(`/api/identities/${id}`)
  if (!res.ok) throw new Error('Identity not found')
  return res.json()
}

export async function createIdentity(payload: Partial<Identity>): Promise<Identity> {
  const res = await fetch('/api/identities', { method: 'POST', headers, body: JSON.stringify(payload) })
  if (!res.ok) throw new Error('Failed to create identity')
  return res.json()
}

export async function updateIdentity(id: string, payload: Partial<Identity>): Promise<Identity> {
  const res = await fetch(`/api/identities/${id}`, { method: 'PUT', headers, body: JSON.stringify(payload) })
  if (!res.ok) throw new Error('Failed to update identity')
  return res.json()
}

export async function deleteIdentity(id: string): Promise<void> {
  const res = await fetch(`/api/identities/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error('Failed to delete identity')
}

// Identity Type API functions
export async function listIdentityTypes(): Promise<IdentityType[]> {
  const res = await fetch('/api/identity-types')
  if (!res.ok) throw new Error('Failed to list identity types')
  return res.json()
}

export async function getIdentityType(name: string): Promise<IdentityType> {
  const res = await fetch(`/api/identity-types/${name}`)
  if (!res.ok) throw new Error('Identity type not found')
  return res.json()
}

export async function getAttributesForType(typeName: string): Promise<AttributeType[]> {
  const res = await fetch(`/api/identity-types/${typeName}/attributes`)
  if (!res.ok) throw new Error('Failed to get attributes for type')
  return res.json()
}


