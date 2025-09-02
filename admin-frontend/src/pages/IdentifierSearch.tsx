import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { 
  IdentifierType, 
  IdentityIdentifier, 
  getSearchableIdentifierTypes, 
  searchIdentifiersByValue, 
  searchIdentifiers,
  getIdentifierSuggestions,
  findIdentifierByTypeAndValue
} from '../api/client'

const IdentifierSearch: React.FC = () => {
  const [identifierTypes, setIdentifierTypes] = useState<IdentifierType[]>([])
  const [searchResults, setSearchResults] = useState<IdentityIdentifier[]>([])
  const [suggestions, setSuggestions] = useState<IdentityIdentifier[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const [searchForm, setSearchForm] = useState({
    searchValue: '',
    identifierTypeId: '',
    verified: undefined as boolean | undefined,
    primary: undefined as boolean | undefined
  })

  const [quickSearch, setQuickSearch] = useState('')

  useEffect(() => {
    loadIdentifierTypes()
  }, [])

  // Auto-suggestions for quick search
  useEffect(() => {
    if (quickSearch.length >= 2) {
      const timeoutId = setTimeout(async () => {
        try {
          const suggestions = await getIdentifierSuggestions(quickSearch)
          setSuggestions(suggestions.slice(0, 5)) // Limit to 5 suggestions
        } catch (err) {
          console.error('Failed to get suggestions:', err)
        }
      }, 300) // Debounce for 300ms

      return () => clearTimeout(timeoutId)
    } else {
      setSuggestions([])
    }
  }, [quickSearch])

  const loadIdentifierTypes = async () => {
    try {
      const types = await getSearchableIdentifierTypes()
      setIdentifierTypes(types)
    } catch (err) {
      console.error('Failed to load identifier types:', err)
      setError(err instanceof Error ? err.message : 'Failed to load identifier types')
    }
  }

  const handleQuickSearch = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!quickSearch.trim()) return

    try {
      setLoading(true)
      setError(null)
      const results = await searchIdentifiersByValue(quickSearch.trim())
      setSearchResults(results)
      setSuggestions([])
    } catch (err) {
      console.error('Search failed:', err)
      setError(err instanceof Error ? err.message : 'Search failed')
      setSearchResults([])
    } finally {
      setLoading(false)
    }
  }

  const handleAdvancedSearch = async (e: React.FormEvent) => {
    e.preventDefault()

    try {
      setLoading(true)
      setError(null)
      
      const searchParams: any = {}
      if (searchForm.searchValue.trim()) searchParams.identifierValue = searchForm.searchValue.trim()
      if (searchForm.identifierTypeId) searchParams.identifierTypeId = searchForm.identifierTypeId
      if (searchForm.verified !== undefined) searchParams.verified = searchForm.verified
      if (searchForm.primary !== undefined) searchParams.primary = searchForm.primary

      const results = await searchIdentifiers(searchParams)
      setSearchResults(results)
    } catch (err) {
      console.error('Advanced search failed:', err)
      setError(err instanceof Error ? err.message : 'Advanced search failed')
      setSearchResults([])
    } finally {
      setLoading(false)
    }
  }

  const handleSuggestionClick = (suggestion: IdentityIdentifier) => {
    setQuickSearch(suggestion.identifierValue)
    setSuggestions([])
    // Automatically search for this value
    searchIdentifiersByValue(suggestion.identifierValue).then(results => {
      setSearchResults(results)
    }).catch(err => {
      console.error('Search failed:', err)
      setError(err instanceof Error ? err.message : 'Search failed')
    })
  }

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setSearchForm(prev => ({
      ...prev,
      [name]: value === '' ? undefined : (
        name === 'verified' || name === 'primary' ? value === 'true' : value
      )
    }))
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Identifier Search</h1>
        
        {error && (
          <div className="mb-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            <strong className="font-bold">Error!</strong>
            <span className="block sm:inline"> {error}</span>
          </div>
        )}

        {/* Quick Search */}
        <div className="bg-white shadow-md rounded-lg p-6 mb-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Quick Search</h2>
          <form onSubmit={handleQuickSearch} className="relative">
            <div className="flex gap-4">
              <div className="flex-1 relative">
                <input
                  type="text"
                  value={quickSearch}
                  onChange={(e) => setQuickSearch(e.target.value)}
                  placeholder="Enter any identifier value (SSN, passport, etc.)"
                  className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
                
                {/* Suggestions dropdown */}
                {suggestions.length > 0 && (
                  <div className="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg">
                    {suggestions.map((suggestion, index) => (
                      <button
                        key={index}
                        type="button"
                        onClick={() => handleSuggestionClick(suggestion)}
                        className="w-full text-left px-4 py-2 hover:bg-gray-100 focus:bg-gray-100 focus:outline-none"
                      >
                        <div className="font-medium">{suggestion.identifierValue}</div>
                        <div className="text-sm text-gray-500">
                          {suggestion.identifierTypeDisplayName}
                          {suggestion.primary && <span className="ml-2 text-blue-600">(Primary)</span>}
                          {suggestion.verified && <span className="ml-2 text-green-600">(Verified)</span>}
                        </div>
                      </button>
                    ))}
                  </div>
                )}
              </div>
              <button
                type="submit"
                disabled={loading || !quickSearch.trim()}
                className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition duration-200"
              >
                {loading ? 'Searching...' : 'Search'}
              </button>
            </div>
          </form>
        </div>

        {/* Advanced Search */}
        <div className="bg-white shadow-md rounded-lg p-6 mb-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Advanced Search</h2>
          <form onSubmit={handleAdvancedSearch} className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="searchValue" className="block text-sm font-medium text-gray-700 mb-2">
                Identifier Value
              </label>
              <input
                type="text"
                id="searchValue"
                name="searchValue"
                value={searchForm.searchValue}
                onChange={handleFormChange}
                placeholder="Partial or full identifier value"
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>

            <div>
              <label htmlFor="identifierTypeId" className="block text-sm font-medium text-gray-700 mb-2">
                Identifier Type
              </label>
              <select
                id="identifierTypeId"
                name="identifierTypeId"
                value={searchForm.identifierTypeId}
                onChange={handleFormChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">All Types</option>
                {identifierTypes.map(type => (
                  <option key={type.id} value={type.id}>{type.displayName}</option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="verified" className="block text-sm font-medium text-gray-700 mb-2">
                Verification Status
              </label>
              <select
                id="verified"
                name="verified"
                value={searchForm.verified === undefined ? '' : searchForm.verified.toString()}
                onChange={handleFormChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">Any</option>
                <option value="true">Verified</option>
                <option value="false">Not Verified</option>
              </select>
            </div>

            <div>
              <label htmlFor="primary" className="block text-sm font-medium text-gray-700 mb-2">
                Primary Status
              </label>
              <select
                id="primary"
                name="primary"
                value={searchForm.primary === undefined ? '' : searchForm.primary.toString()}
                onChange={handleFormChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">Any</option>
                <option value="true">Primary</option>
                <option value="false">Not Primary</option>
              </select>
            </div>

            <div className="md:col-span-2">
              <button
                type="submit"
                disabled={loading}
                className="px-6 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition duration-200"
              >
                {loading ? 'Searching...' : 'Advanced Search'}
              </button>
            </div>
          </form>
        </div>

        {/* Search Results */}
        <div className="bg-white shadow-md rounded-lg overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">
              Search Results ({searchResults.length})
            </h2>
          </div>

          {searchResults.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Identifier
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Type
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Identity
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {searchResults.map((identifier) => (
                    <tr key={identifier.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {identifier.identifierValue}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">
                          {identifier.identifierTypeDisplayName}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <Link
                          to={`/identities/${identifier.identityId}`}
                          className="text-blue-600 hover:text-blue-900 transition duration-200"
                        >
                          View Identity
                        </Link>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
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
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <Link
                          to={`/identities/${identifier.identityId}`}
                          className="text-indigo-600 hover:text-indigo-900 transition duration-200"
                        >
                          View Details
                        </Link>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="px-6 py-8 text-center">
              <p className="text-gray-500">
                {loading ? 'Searching...' : 'No identifiers found. Try a different search.'}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default IdentifierSearch
