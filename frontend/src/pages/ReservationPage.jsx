import axios from 'axios';
import React, { useState, useEffect } from 'react';
import api from '../api/axios';

function ReservationPage() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [messages, setMessages] = useState({});

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/products');
            setProducts(response.data);
            setLoading(false);
        } catch (error) {
            console.error("Failed to fetch products", error);
            setLoading(false);
        }
    };

    const handleReserve = async (productId) => {
        try {
            await api.post('/reservations', {
                memberId: 5, // Hardcoded for demo
                productId: productId,
                quantity: 1
            });
            setMessages(prev => ({ ...prev, [productId]: { type: 'success', text: 'Reservation Successful!' } }));
            // Refresh products to show updated stock (optional, since dashboard is real-time)
            fetchProducts();
        } catch (error) {
            console.error("Reservation failed", error);
            setMessages(prev => ({ ...prev, [productId]: { type: 'error', text: 'Reservation Failed.' } }));
        }

        // Clear message after 3 seconds
        setTimeout(() => {
            setMessages(prev => {
                const newState = { ...prev };
                delete newState[productId];
                return newState;
            });
        }, 3000);
    };

    if (loading) return <div className="layout">Loading...</div>;

    return (
        <div className="layout">
            <h1 style={{ marginBottom: '2rem' }}>Available Products</h1>
            <div className="product-grid">
                {products.map(product => (
                    <div key={product.id} className="card">
                        <h2>{product.name}</h2>
                        <div style={{ margin: '1rem 0', color: 'var(--text-muted)' }}>
                            <p>Price: ${product.price}</p>
                            <p>Stock: {product.stock}</p>
                        </div>
                        {messages[product.id] && (
                            <div style={{
                                padding: '0.5rem',
                                borderRadius: '0.25rem',
                                marginBottom: '1rem',
                                background: messages[product.id].type === 'success' ? 'rgba(16, 185, 129, 0.2)' : 'rgba(239, 68, 68, 0.2)',
                                color: messages[product.id].type === 'success' ? '#34d399' : '#f87171'
                            }}>
                                {messages[product.id].text}
                            </div>
                        )}
                        <button
                            className="btn btn-primary"
                            disabled={product.stock <= 0}
                            onClick={() => handleReserve(product.id)}
                            style={{ width: '100%', opacity: product.stock <= 0 ? 0.5 : 1 }}
                        >
                            {product.stock > 0 ? 'Reserve Now' : 'Out of Stock'}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ReservationPage;
