import { useState, useEffect } from 'react';
import './ProductModal.css';

function ProductModal({ isOpen, onClose, onSave, onDelete, editProduct }) {
  const [name, setName] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    if (editProduct) {
      setName(editProduct.product.name);
    } else {
      setName('');
    }
  }, [editProduct]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    setSaving(true);
    setError('');

    try {
      await onSave({ name: name.trim() }, editProduct);
      setName('');
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to save product');
    } finally {
      setSaving(false);
    }
  };

  const handleClose = () => {
    setName('');
    setError('');
    onClose();
  };

  const handleDelete = async () => {
    if (!editProduct) return;

    setDeleting(true);
    setError('');

    try {
      await onDelete(editProduct);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to delete product');
    } finally {
      setDeleting(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{editProduct ? 'Edit Album' : 'Add Album'}</h2>
          <button className="close-btn" onClick={handleClose}>&times;</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="productName">Album Name:</label>
            <input
              id="productName"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter album name..."
              autoFocus
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <div className="modal-actions">
            <button type="submit" disabled={saving || deleting}>
              {saving ? 'Saving...' : 'Save'}
            </button>
            {editProduct && (
              <button
                type="button"
                className="delete-btn"
                onClick={handleDelete}
                disabled={saving || deleting}
              >
                {deleting ? 'Deleting...' : 'Delete'}
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}

export default ProductModal;
