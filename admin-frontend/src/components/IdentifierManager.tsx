import React, { useState, useEffect } from 'react'
import {
  IdentifierType,
  IdentityIdentifier,
  getSearchableIdentifierTypes,
  getIdentifiersForIdentity,
  createIdentifier,
  updateIdentifier,
  deleteIdentifier,
  verifyIdentifier
} from '../api/client'

interface IdentifierManagerProps {
  identityId: string
}

const IdentifierManager: React.FC<IdentifierManagerProps> = ({ identityId }) => {
  const [identifierTypes, setIdentifierTypes] = useState<IdentifierType[]>([])
  const [identifiers, setIdentifiers] = useState<IdentityIdentifier[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showAddForm, setShowAddForm] = useState(false)
  const [editingId, setEditingId] = useState<string | null>(null)

  const [addForm, setAddForm] = useState({
    identifierTypeId: '',
    identifierValue: '',
    primary: false
  })

  const [editForm, setEditForm] = useState({
    identifierValue: '',
    primary: false
  })

  useEffect(() => {
    loadData()
  }, [identityId])

  const loadData = async () => {
    try {
      setLoading(true)
      const [types, identifierList] = await Promise.all([
        getSearchableIdentifierTypes(),
        getIdentifiersForIdentity(identityId)
      ])
      setIdentifierTypes(types)
      setIdentifiers(identifierList)
      setError(null)
    } catch (err) {
      console.error('Failed to load data:', err)
      setError(err instanceof Error ? err.message : 'Failed to load data')
    } finally {
      setLoading(false)
    }
  }

  const handleAddSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    try {
      await createIdentifier({
        identityId,
        identifierTypeId: addForm.identifierTypeId,
        identifierValue: addForm.identifierValue,
        primary: addForm.primary
      })

      // Reset form and reload data
      setAddForm({ identifierTypeId: '', identifierValue: '', primary: false })
      setShowAddForm(false)
      await loadData()
    } catch (err) {
      console.error('Failed to add identifier:', err)
      alert(`Failed to add identifier: ${err instanceof Error ? err.message : 'Unknown error'}`)
    }
  }

  const handleEditSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!editingId) return

    try {
      await updateIdentifier(editingId, editForm.identifierValue, editForm.primary)
      setEditingId(null)
      await loadData()
    } catch (err) {
      console.error('Failed to update identifier:', err)
      alert(`Failed to update identifier: ${err instanceof Error ? err.message : 'Unknown error'}`)
    }
  }

  const handleDelete = async (id: string, value: string) => {
    if (!confirm(`Are you sure you want to delete the identifier "${value}"?`)) {
      return
    }

    try {
      await deleteIdentifier(id)
      await loadData()
    } catch (err) {
      console.error('Failed to delete identifier:', err)
      alert(`Failed to delete identifier: ${err instanceof Error ? err.message : 'Unknown error'}`)
    }
  }

  const handleVerify = async (id: string, value: string) => {
    const verifiedBy = prompt(`Verify identifier "${value}". Enter your name/ID:`)
    if (!verifiedBy) return

    try {
      await verifyIdentifier(id, verifiedBy)
      await loadData()
    } catch (err) {
      console.error('Failed to verify identifier:', err)
      alert(`Failed to verify identifier: ${err instanceof Error ? err.message : 'Unknown error'}`)
    }
  }

  const startEdit = (identifier: IdentityIdentifier) => {
    setEditingId(identifier.id)
    setEditForm({
      identifierValue: identifier.identifierValue,
      primary: identifier.primary
    })
  }

  const cancelEdit = () => {
    setEditingId(null)
    setEditForm({ identifierValue: '', primary: false })
  }

  if (loading) {
    return (
      <div className="bg-white shadow-md rounded-lg p-6">
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="space-y-3">
            <div className="h-4 bg-gray-200 rounded"></div>
            <div className="h-4 bg-gray-200 rounded w-3/4"></div>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="bg-white shadow-md rounded-lg p-6">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          <strong className="font-bold">Error!</strong>
          <span className="block sm:inline"> {error}</span>
        </div>
      </div>
    )
  }

  return (
    <div className="bg-white shadow-md rounded-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h3 className="text-lg font-medium text-gray-900">Identifiers</h3>
        <button
          onClick={() => setShowAddForm(!showAddForm)}
          className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded text-sm transition duration-200"
        >
          {showAddForm ? 'Cancel' : 'Add Identifier'}
        </button>
      </div>

      {/* Add Form */}
      {showAddForm && (
        <form onSubmit={handleAddSubmit} className="mb-6 p-4 bg-gray-50 rounded-lg">
          <h4 className="text-md font-medium text-gray-900 mb-4">Add New Identifier</h4>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label htmlFor="identifierTypeId" className="block text-sm font-medium text-gray-700 mb-2">
                Type *
              </label>
              <select
                id="identifierTypeId"
                value={addForm.identifierTypeId}
                onChange={(e) => setAddForm(prev => ({ ...prev, identifierTypeId: e.target.value }))}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">Select Type</option>
                {identifierTypes.map(type => (
                  <option key={type.id} value={type.id}>{type.displayName}</option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="identifierValue" className="block text-sm font-medium text-gray-700 mb-2">
                Value *
              </label>
              <input
                type="text"
                id="identifierValue"
                value={addForm.identifierValue}
                onChange={(e) => setAddForm(prev => ({ ...prev, identifierValue: e.target.value }))}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter identifier value"
              />
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                id="primary"
                checked={addForm.primary}
                onChange={(e) => setAddForm(prev => ({ ...prev, primary: e.target.checked }))}
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label htmlFor="primary" className="ml-2 block text-sm text-gray-900">
                Primary Identifier
              </label>
            </div>
          </div>

          <div className="mt-4 flex justify-end space-x-2">
            <button
              type="button"
              onClick={() => setShowAddForm(false)}
              className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition duration-200"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 transition duration-200"
            >
              Add Identifier
            </button>
          </div>
        </form>
      )}

      {/* Identifiers List */}
      {identifiers.length > 0 ? (
        <div className="space-y-4">
          {identifiers.map((identifier) => (
            <div key={identifier.id} className="border border-gray-200 rounded-lg p-4">
              {editingId === identifier.id ? (
                // Edit Form
                <form onSubmit={handleEditSubmit}>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Value *
                      </label>
                      <input
                        type="text"
                        value={editForm.identifierValue}
                        onChange={(e) => setEditForm(prev => ({ ...prev, identifierValue: e.target.value }))}
                        required
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        checked={editForm.primary}
                        onChange={(e) => setEditForm(prev => ({ ...prev, primary: e.target.checked }))}
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <label className="ml-2 block text-sm text-gray-900">
                        Primary Identifier
                      </label>
                    </div>
                  </div>
                  <div className="flex justify-end space-x-2">
                    <button
                      type="button"
                      onClick={cancelEdit}
                      className="px-3 py-1 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50 transition duration-200"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      className="px-3 py-1 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 transition duration-200"
                    >
                      Save
                    </button>
                  </div>
                </form>
              ) : (
                // Display Mode
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-2 mb-2">
                      <span className="text-lg font-medium text-gray-900">
                        {identifier.identifierValue}
                      </span>
                      <div className="flex flex-wrap gap-1">
                        {identifier.primary && (
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                            Primary
                          </span>
                        )}
                        {identifier.verified && (
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                            Verified
                          </span>
                        )}
                        {!identifier.verified && (
                          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                            Unverified
                          </span>
                        )}
                      </div>
                    </div>
                    <p className="text-sm text-gray-600">{identifier.identifierTypeDisplayName}</p>
                    {identifier.verified && identifier.verifiedBy && (
                      <p className="text-xs text-gray-500 mt-1">
                        Verified by {identifier.verifiedBy}
                        {identifier.verifiedAt && ` on ${new Date(identifier.verifiedAt).toLocaleDateString()}`}
                      </p>
                    )}
                  </div>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => startEdit(identifier)}
                      className="text-blue-600 hover:text-blue-900 text-sm transition duration-200"
                    >
                      Edit
                    </button>
                    {!identifier.verified && (
                      <button
                        onClick={() => handleVerify(identifier.id, identifier.identifierValue)}
                        className="text-green-600 hover:text-green-900 text-sm transition duration-200"
                      >
                        Verify
                      </button>
                    )}
                    <button
                      onClick={() => handleDelete(identifier.id, identifier.identifierValue)}
                      className="text-red-600 hover:text-red-900 text-sm transition duration-200"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center py-8">
          <p className="text-gray-500">No identifiers found for this identity.</p>
          {!showAddForm && (
            <button
              onClick={() => setShowAddForm(true)}
              className="mt-4 inline-block bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200"
            >
              Add the first identifier
            </button>
          )}
        </div>
      )}
    </div>
  )
}

export default IdentifierManager
