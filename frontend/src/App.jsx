import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ReservationPage from './pages/ReservationPage';
import AdminDashboard from './pages/AdminDashboard';

function App() {
    return (
        <Router>
            <div className="layout">
                <header className="header">
                    <Link to="/" className="logo">TrafficReserve</Link>
                    <nav>
                        <Link to="/" className="nav-link">Reserve</Link>
                        <Link to="/admin" className="nav-link">Admin Dashboard</Link>
                    </nav>
                </header>

                <main>
                    <Routes>
                        <Route path="/" element={<ReservationPage />} />
                        <Route path="/admin" element={<AdminDashboard />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

export default App;
