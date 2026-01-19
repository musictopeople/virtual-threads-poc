import { useState, useEffect } from 'react';
import ProductModal from './ProductModal';
import './ProductList.css';

const API_BASE_URL = 'http://localhost:8080';

function ProductList() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize] = useState(5);
  const [totalCount, setTotalCount] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  useEffect(() => {
    fetchProducts().then();
  }, [searchTerm, page]);

  const fetchProducts = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = new URLSearchParams({
        'filter.name': searchTerm,
        'page.offset': page,
        'page.size': pageSize
      });

      const response = await fetch(`${API_BASE_URL}/product?${params}`);
      if (!response.ok) {
        throw new Error('Failed to fetch products');
      }

      const result = await response.json();
      setProducts(result.data || []);
      setTotalCount(result.meta?.count || 0);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const totalPages = Math.ceil(totalCount / pageSize);

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setPage(0); // Reset to first page on new search
  };

  const handlePrevPage = () => {
    if (page > 0) setPage(page - 1);
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) setPage(page + 1);
  };

  const handleSaveProduct = async (product, editProduct) => {
    const isEditing = !!editProduct;

    if (isEditing) {
      const url = `${API_BASE_URL}/product/${editProduct.id}/${editProduct.version}`;
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
      });

      if (!response.ok) {
        throw new Error('Failed to update product');
      }

      console.log('Product updated:', { id: editProduct.id, version: editProduct.version, product });
    } else {
      const url = `${API_BASE_URL}/product`;
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
      });

      if (!response.ok) {
        throw new Error('Failed to create product');
      }
    }

    // Refresh the product list
    await fetchProducts();
  };

  const handleEditProduct = (product) => {
    setEditingProduct(product);
    setIsModalOpen(true);
  };

  const handleDeleteProduct = async (product) => {
    const url = `${API_BASE_URL}/product/${product.id}/${product.version}`;
    const response = await fetch(url, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error('Failed to delete product');
    }

    console.log('Product deleted:', { id: product.id, version: product.version });

    // Refresh the product list
    await fetchProducts();
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingProduct(null);
  };

  if (loading && products.length === 0) {
    return <div className="loading">Loading products...</div>;
  }

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
    <div className="product-list-container">
      <div className="header">
        <h1>Free Jazz!!</h1>
        <button onClick={() => { setEditingProduct(null); setIsModalOpen(true); }} className="add-btn">
          Add Album
        </button>
      </div>

      <input
        type="text"
        placeholder="Search for the hits..."
        value={searchTerm}
        onChange={handleSearchChange}
        className="search-input"
      />

      <ul className="product-grid">
        {products.length === 0 ? (
          <li>No products found</li>
        ) : (
          products.map((product) => (
            <li
              key={product.id}
              className="product-card"
              onClick={() => handleEditProduct(product)}
            >
              {product.product.name}
            </li>
          ))
        )}
      </ul>

      <div className="pagination">
        <button
          onClick={handlePrevPage}
          disabled={page === 0}
          className="pagination-btn"
        >
          Previous
        </button>
        <span>
          Page {page + 1} of {totalPages || 1} ({totalCount} items)
        </span>
        <button
          onClick={handleNextPage}
          disabled={page >= totalPages - 1}
          className="pagination-btn"
        >
          Next
        </button>
      </div>

      <ProductModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSave={handleSaveProduct}
        onDelete={handleDeleteProduct}
        editProduct={editingProduct}
      />
    </div>
  );
}

export default ProductList;
