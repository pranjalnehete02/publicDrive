import React, { useState } from 'react';
import axios from 'axios';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

export default function ForgotPassword() {
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

   const handleSubmit = async (e) => {
    e.preventDefault();


    try {
        // Send a POST request with email as a URL parameter
        await axios.post(`http://localhost:8081/auth/forgot-password?email=${encodeURIComponent(email)}`);

        // Show success message
        toast.success("Reset link sent! Check your email.");

        // Redirect to the login page
        navigate("/login");
    } catch (error) {
        // Show error message if something goes wrong
        toast.error(error.response?.data?.message || "Something went wrong");
    }
};


    return (
        <div className="forgot-password-container">
            <h2>Forgot Password</h2>
            <form onSubmit={handleSubmit}>
                <input 
                    type="email" 
                    placeholder="Enter your email" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    required 
                />
                <button type="submit">Send Reset Link</button>
            </form>
        </div>
    );
}
