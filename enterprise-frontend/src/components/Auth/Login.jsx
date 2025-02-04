import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { addUser } from "../../reduxstore/userSlice";
import * as Components from "./Components.jsx";
import "./style1.css";
import axios from "axios";
import toast from 'react-hot-toast';

export default function Login() {
  const [signIn, toggle] = useState(true); // Toggle between login and signup
  const [forgotPassword, setForgotPassword] = useState(false); // Toggle for forgot password
  const navigate = useNavigate();
  const [user, setUser] = useState({ "username": "", "password": "", "email": "" });
  const dispatcher = useDispatch();
  console.log(process.env.REACT_APP_API_URL,"lkgjfdklsjgdalkfjkldjflasfjdlksjfldasjl")
  const handleSignUp = () => {
    axios.post(`${process.env.REACT_APP_API_URL}/auth/signup`, user)
      .then(res => {
        toast.success("Signup Successful");
      }).catch(err => {
        toast.error(err.message);
      });
  };

  const handleLogin = () => {
    axios.post(`${process.env.REACT_APP_API_URL}/auth/login`, user)
      .then(res => {
        localStorage.setItem("token", res.data.token);
        dispatcher(addUser(user.username));
        toast.success("Login Successful");
        navigate("/homepage");
      }).catch(err => {
        toast.error(err.message);
      });
  };

  const handleForgotPassword = () => {
    axios.post(`${process.env.REACT_APP_API_URL}/auth/forgot-password`, { email: user.email })
      .then(res => {
        toast.success("Password reset link sent!");
      }).catch(err => {
        toast.error(err.message);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  return (
    <div className="centered-container">
      <Components.Container>
        {/* Forgot Password Form */}
        {forgotPassword ? (
          <Components.ForgotPasswordContainer active={forgotPassword}>
            <Components.Form>
              <Components.Title>Forgot Password</Components.Title>
              <Components.Input type="email" name="email" placeholder="Enter your email" onChange={handleInputChange} />
              <Components.Button type="button" onClick={handleForgotPassword}>Reset Password</Components.Button>
              <Components.Anchor href="#" onClick={() => setForgotPassword(false)}>Back to Login</Components.Anchor>
            </Components.Form>
          </Components.ForgotPasswordContainer>
        ) : (
          <>
            {/* Sign Up Form */}
            <Components.SignUpContainer signingIn={signIn}>
              <Components.Form>
                <Components.Title>Create Account</Components.Title>
                <Components.Input type="text" name="username" placeholder="Name" onChange={handleInputChange} />
                <Components.Input type="email" name="email" placeholder="Email" onChange={handleInputChange} />
                <Components.Input type="password" name="password" placeholder="Password" onChange={handleInputChange} />
                <Components.Button onClick={handleSignUp}>Sign Up</Components.Button>
              </Components.Form>
            </Components.SignUpContainer>

            {/* Login Form */}
            <Components.SignInContainer signingIn={signIn}>
              <Components.Form>
                <Components.Title>Sign in</Components.Title>
                <Components.Input type="text" name="username" onChange={handleInputChange} placeholder="Username" />
                <Components.Input type="password" name="password" onChange={handleInputChange} placeholder="Password" />
                <Components.Anchor href="#" onClick={() => setForgotPassword(true)}>Forgot your password?</Components.Anchor>
                <Components.Button type="button" onClick={handleLogin}>Sign In</Components.Button>
              </Components.Form>
            </Components.SignInContainer>

            {/* Overlay */}
            <Components.OverlayContainer signingIn={signIn}>
              <Components.Overlay signingIn={signIn}>
                <Components.LeftOverlayPanel signingIn={signIn}>
                  <Components.Title>Welcome Back!</Components.Title>
                  <Components.Paragraph>
                    To keep connected with us please login with your personal info
                  </Components.Paragraph>
                  <Components.GhostButton onClick={() => toggle(true)}>Sign In</Components.GhostButton>
                </Components.LeftOverlayPanel>
                <Components.RightOverlayPanel signingIn={signIn}>
                  <Components.Title>Hello, Friend!</Components.Title>
                  <Components.Paragraph>
                    Enter your personal details and start your journey with us
                  </Components.Paragraph>
                  <Components.GhostButton onClick={() => toggle(false)}>Sign Up</Components.GhostButton>
                </Components.RightOverlayPanel>
              </Components.Overlay>
            </Components.OverlayContainer>
          </>
        )}
      </Components.Container>
    </div>
  );
}
