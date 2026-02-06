import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import api from '../api/axios';

function AdminDashboard() {
    const [data, setData] = useState([]);
    const [logs, setLogs] = useState([]);

    useEffect(() => {
        // Initial fetch
        fetchData();

        // WebSocket Setup
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            onConnect: () => {
                console.log('Connected to WebSocket');
                client.subscribe('/topic/logs', (message) => {
                    const msgBody = message.body;
                    console.log('Received log:', msgBody);
                    setLogs(prev => [msgBody, ...prev.slice(0, 9)]); // Keep last 10 logs
                    fetchData(); // Refresh data on new reservation
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    const fetchData = async () => {
        try {
            // Re-using products endpoint for the chart as user requested "stock quantity"
            // If AdminStatsResponse provides better aggregated data, we could use that. 
            // Checking AdminStatsResponse content... assuming it might be generic stats.
            // For stock chart, /products matches the requirement best.
            const response = await api.get('/products');
            setData(response.data);
        } catch (error) {
            console.error("Failed to fetch data", error);
        }
    };

    return (
        <div className="layout dashboard-container">
            <div>
                <h1 style={{ marginBottom: '2rem' }}>Real-time Stock Dashboard</h1>
                <div className="card" style={{ height: '400px' }}>
                    <ResponsiveContainer width="100%" height="100%">
                        <BarChart data={data}>
                            <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                            <XAxis dataKey="name" stroke="#94a3b8" />
                            <YAxis stroke="#94a3b8" />
                            <Tooltip
                                contentStyle={{ backgroundColor: '#1e293b', border: 'none', borderRadius: '0.5rem' }}
                                itemStyle={{ color: '#f8fafc' }}
                            />
                            <Legend />
                            <Bar dataKey="stock" fill="#8884d8" name="Remaining Stock">
                                {
                                    data.map((entry, index) => (
                                        <cell key={`cell-${index}`} fill={index % 2 === 0 ? '#4f46e5' : '#ec4899'} />
                                    ))
                                }
                            </Bar>
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>

            <div>
                <h2 style={{ marginBottom: '1rem' }}>Live Logs</h2>
                <div className="card" style={{ height: '400px', overflowY: 'auto' }}>
                    {logs.length === 0 ? <p style={{ color: '#94a3b8' }}>Waiting for reservations...</p> : (
                        <ul style={{ listStyle: 'none', padding: 0 }}>
                            {logs.map((log, index) => (
                                <li key={index} style={{
                                    padding: '0.75rem',
                                    borderBottom: '1px solid rgba(255,255,255,0.05)',
                                    fontSize: '0.875rem'
                                }}>
                                    <span style={{ color: '#34d399', marginRight: '0.5rem' }}>●</span>
                                    {log}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>
        </div>
    );
}

export default AdminDashboard;
