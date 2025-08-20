import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { IdentityType, getIdentityType } from '../api/client'

interface IdentityTypeFormData {
  name: string
  displayName: string
  description: string
  active: boolean
}

export default function IdentityTypeForm() {
  const navigate = useNavigate()
  const { name } = useParams<{ name: string }>()
  const isEditMode = !!name

  const [formData, setFormData] = useState<IdentityTypeFormData>({
    name: '',
    displayName: '',
    description: '',
    active: true
  })
  const [loading, setLoading] = useState(isEditMode)
  const [error, setError] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (isEditMode) {
      loadIdentityType()
    }
  }, [name])

  async function loadIdentityType() {
    try {
      setLoading(true)
      const data = await getIdentityType(name!)
      setFormData({
        name: data.name,
        displayName: data.displayName,
        description: data.description || '',
        active: data.active
      })
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load identity type')
    } finally {
      setLoading(false)
    }
  }

  function handleInputChange(field: keyof IdentityTypeFormData, value: any) {
    setFormData(prev => ({ ...prev, [field]: value }))
  }



  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    
    if (!formData.name || !formData.displayName) {
      alert('Please fill in all required fields')
      return
    }

    try {
      setSaving(true)
      
      // TODO: Implement actual API calls for create/update
      // For now, we'll just simulate the operation
      console.log('Saving identity type:', formData)
      
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      alert(isEditMode ? 'Identity type updated successfully!' : 'Identity type created successfully!')
      navigate('/types')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to save identity type')
    } finally {
      setSaving(false)
    }
  }

  function handleCancel() {
    if (confirm('Are you sure you want to cancel? All unsaved changes will be lost.')) {
      navigate('/types')
    }
  }

  if (loading) return <div>Loading identity type...</div>
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>{isEditMode ? 'Edit Identity Type' : 'New Identity Type'}</h2>
        <button
          onClick={handleCancel}
          style={{
            background: '#6b7280',
            color: 'white',
            border: 'none',
            padding: '8px 16px',
            borderRadius: 6,
            cursor: 'pointer'
          }}
        >
          Cancel
        </button>
      </div>

      <form onSubmit={handleSubmit} style={{ maxWidth: '800px' }}>
        <div style={{ 
          border: '1px solid #e5e7eb', 
          borderRadius: 8, 
          padding: '1.5rem', 
          marginBottom: '1.5rem',
          background: 'white'
        }}>
          <h3>Basic Information</h3>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Name *
              </label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => handleInputChange('name', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., employee"
                disabled={isEditMode} // Name cannot be changed after creation
              />
              {isEditMode && (
                <small style={{ color: '#6b7280' }}>Name cannot be changed after creation</small>
              )}
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
                Display Name *
              </label>
              <input
                type="text"
                value={formData.displayName}
                onChange={(e) => handleInputChange('displayName', e.target.value)}
                style={{
                  width: '100%',
                  padding: '8px',
                  border: '1px solid #d1d5db',
                  borderRadius: 4
                }}
                placeholder="e.g., Employee"
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '4px', fontWeight: '500' }}>
              Description
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => handleInputChange('description', e.target.value)}
              style={{
                width: '100%',
                padding: '8px',
                border: '1px solid #d1d5db',
                borderRadius: 4,
                minHeight: '80px'
              }}
              placeholder="Description of this identity type"
            />
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                checked={formData.active}
                onChange={(e) => handleInputChange('active', e.target.checked)}
              />
              Active
            </label>
            <small style={{ color: '#6b7280', marginLeft: '24px' }}>
              Inactive types cannot be used for new identities
            </small>
          </div>
        </div>



        <div style={{ marginTop: '1.5rem', display: 'flex', gap: '12px' }}>
          <button
            type="submit"
            disabled={saving}
            style={{
              background: '#2563eb',
              color: 'white',
              border: 'none',
              padding: '12px 24px',
              borderRadius: 6,
              cursor: saving ? 'not-allowed' : 'pointer',
              opacity: saving ? 0.6 : 1
            }}
          >
            {saving ? 'Saving...' : (isEditMode ? 'Update Type' : 'Create Type')}
          </button>
          
          <button
            type="button"
            onClick={handleCancel}
            style={{
              background: '#6b7280',
              color: 'white',
              border: 'none',
              padding: '12px 24px',
              borderRadius: 6,
              cursor: 'pointer'
            }}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  )
}
