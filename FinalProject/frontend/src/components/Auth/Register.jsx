import React, { useState } from 'react';
import { Button, TextField, Container, Typography, Box, Link } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';  // Import axios

const Register = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);  // Add loading state for button disabling
  const [error, setError] = useState('');  // To show error messages
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Passwords don't match!");
      return;
    }

    try {
      setLoading(true);  // Set loading to true while submitting
      setError('');  // Clear any previous errors

      // Make a POST request to your backend to register the user
      const response = await axios.post('http://localhost:8081/api/user/register', {
        username,
        email,
        password
      });

      if (response.status === 201) {
        // On success, navigate to the login page
        navigate('/login');
      }
    } catch (err) {
      console.error(err);
      setError('An error occurred while registering. Please try again.');  // Show error message
    } finally {
      setLoading(false);  // Reset loading state
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography variant="h4" gutterBottom>Register</Typography>
        {error && <Typography color="error" variant="body2">{error}</Typography>} {/* Display error message */}
        <form onSubmit={handleSubmit}>
          <TextField
            label="Username"
            fullWidth
            margin="normal"
            required
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="Email"
            fullWidth
            margin="normal"
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            label="Password"
            fullWidth
            margin="normal"
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <TextField
            label="Confirm Password"
            fullWidth
            margin="normal"
            type="password"
            required
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            error={password !== confirmPassword}
            helperText={password !== confirmPassword ? "Passwords don't match!" : ""}
          />
          <Button 
            type="submit" 
            fullWidth 
            variant="contained" 
            sx={{ mt: 3, mb: 2 }} 
            disabled={loading} // Disable button while loading
          >
            {loading ? 'Registering...' : 'Register'}
          </Button>
          <Link href="/login" variant="body2">
            Already have an account? Login
          </Link>
        </form>
      </Box>
    </Container>
  );
};

export default Register;
