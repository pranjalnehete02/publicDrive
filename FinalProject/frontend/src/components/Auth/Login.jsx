import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { addUser } from "../../reduxstore/userSlice";
import * as Components from "./Components.jsx";
import "./style1.css";
import axios from "axios";
import toast from 'react-hot-toast';

export default function Login() {
  const [signIn, setSignIn] = useState(true);          // Sign In state
  const [forgotPassword, setForgotPassword] = useState(false); // Forgot Password state
  const [user, setUser] = useState({ username: "", password: "", email: "" });
  const dispatcher = useDispatch();
  const navigate = useNavigate();

  // Sign up request
  const handleSignUp = () => {
    axios.post("http://localhost:8081/auth/signup", user)
      .then(res => {
        toast.success("Signup Successful");
      })
      .catch(err => {
        toast.error(err.message);
      });
  };

  // Login request
  const handleLogin = () => {
    axios.post("http://localhost:8081/auth/login", user)
      .then(res => {
        localStorage.setItem("token", res.data.token);
        dispatcher(addUser(user.username));
        toast.success("Login Successful");
        navigate("/homepage");
      })
      .catch(err => {
        toast.error(err.message);
      });
  };

  // Forgot password request
  const handleForgotPassword = (e) => {
    e.preventDefault();

    axios.post(`http://localhost:8081/auth/forgot-password?email=${encodeURIComponent(user.email)}`)
      .then(res => {
        toast.success("Reset link sent! Check your email.");
        setForgotPassword(false); // After reset link sent, go back to Sign In
      })
      .catch(err => {
        toast.error(err.message);
      });
  };

  // Handle input change (for Sign Up, Sign In, and Forgot Password)
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  return (
    <div className="main">
      <Components.Container>
        {/* Sign Up Form */}
        {signIn === false && !forgotPassword && (
          <Components.SignUpContainer signingIn={signIn}>
            <Components.Form>
              <Components.Title>Create Account</Components.Title>
              <Components.Input
                type="text"
                name="username"
                placeholder="Name"
                onChange={handleInputChange}
              />
              <Components.Input
                type="email"
                name="email"
                placeholder="Email"
                onChange={handleInputChange}
              />
              <Components.Input
                type="password"
                name="password"
                placeholder="Password"
                onChange={handleInputChange}
              />
              <Components.Button onClick={handleSignUp}>Sign Up</Components.Button>
            </Components.Form>
          </Components.SignUpContainer>
        )}

        {/* Forgot Password Form */}
        {forgotPassword && (
          <Components.SignUpContainer signingIn={signIn}>
            <Components.Form onSubmit={handleForgotPassword}>
              <Components.Title>Forgot Password</Components.Title>
              <Components.Input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={user.email}
                onChange={handleInputChange}
                required
              />
              <Components.Button type="submit">Send Reset Link</Components.Button>
              <Components.GhostButton onClick={() => setForgotPassword(false)}>
                Back to Login
              </Components.GhostButton>
            </Components.Form>
          </Components.SignUpContainer>
        )}

        {/* Sign In Form */}
        {!forgotPassword && signIn && (
          <Components.SignInContainer signingIn={signIn}>
            <Components.Form>
              <Components.Title>Sign in</Components.Title>
              <Components.Input
                type="text"
                name="username"
                placeholder="Username"
                onChange={handleInputChange}
              />
              <Components.Input
                type="password"
                name="password"
                placeholder="Password"
                onChange={handleInputChange}
              />
              <Components.Anchor href="#" onClick={() => {setForgotPassword(true); setSignIn(false)}}>Forgot your password?</Components.Anchor>
              <Components.Button type="button" onClick={handleLogin}>
                Sign In
              </Components.Button>

              {/* Forgot Password Button */}
              
            </Components.Form>
          </Components.SignInContainer>
        )}

        {/* Overlay */}
        <Components.OverlayContainer signingIn={signIn}>
          <Components.Overlay signingIn={signIn}>
            <Components.LeftOverlayPanel signingIn={signIn}>
              <Components.Title>Welcome Back!</Components.Title>
              <Components.Paragraph>
                To keep connected with us, please login with your personal info.
              </Components.Paragraph>
              <Components.GhostButton onClick={() =>{ setSignIn(true); setForgotPassword(false)}}>
                Sign In
              </Components.GhostButton>
              <Components.GhostButton className="forgotpassword1" onClick={() => setForgotPassword(true)}>
                Forgot Password
              </Components.GhostButton>
            </Components.LeftOverlayPanel>
            <Components.RightOverlayPanel signingIn={signIn}>
              <Components.Title>Hello, Friend!</Components.Title>
              <Components.Paragraph>
                Enter your personal details and start your journey with us.
              </Components.Paragraph>
              <Components.GhostButton onClick={() => setSignIn(false)}>
                Sign Up
              </Components.GhostButton>
              
            </Components.RightOverlayPanel>
          </Components.Overlay>
        </Components.OverlayContainer>
      </Components.Container>
    </div>
  );
}
