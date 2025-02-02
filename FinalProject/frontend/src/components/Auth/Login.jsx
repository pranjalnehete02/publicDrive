import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import {addUser} from "../../reduxstore/userSlice"
import * as Components from "./Components.jsx";
import "./style1.css";
import axios from "axios";
import toast from 'react-hot-toast';
export default function Login() {
  const [signIn, toggle] = React.useState(true);
  const navigate = useNavigate();
  const [user,setUser] = useState({"username":"","password":"","email":""})
  const dispatcher = useDispatch();
  const handleSignUp=()=>{
    
    axios.post("http://localhost:8081/auth/signup",user)
    .then(res=>{
      toast.success("Signup Successfull");
      
    }).catch(err=>{
      toast.error(err.message)
    })
  }
  const handleLogin=()=>{
    axios.post("http://localhost:8081/auth/login",user)
    .then(res=>{
      console.log(res.data)
      localStorage.setItem("token",res.data.token);
      dispatcher(addUser(user.username))
      toast.success("Login Successfull");
      navigate("/homepage");

    }).catch(err=>{
      toast.error(err.message)
    })
  }
  const handleInputChange = (e) => {
    const { name, value } = e.target;
   
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value, 
    }));
  };

  return (
    <Components.Container>
      <Components.SignUpContainer signingIn={signIn}>
        <Components.Form>
          <Components.Title>Create Account</Components.Title>
          <Components.Input type="text" name="username" placeholder="Name" onChange={handleInputChange} />
          <Components.Input type="email" name="email" placeholder="Email" onChange={handleInputChange} />
          <Components.Input type="password" name="password" placeholder="Password" onChange={handleInputChange} />
          <Components.Button onClick={()=>{handleSignUp()}}>Sign Up</Components.Button>
        </Components.Form>
      </Components.SignUpContainer>
      <Components.SignInContainer signingIn={signIn}>
        <Components.Form >
          <Components.Title>Sign in</Components.Title>
          <Components.Input type="text" name="username" onChange={handleInputChange} placeholder="username" />
          <Components.Input type="password" name="password" onChange={handleInputChange} placeholder="Password" />
          <Components.Anchor href="#">Forgot your password?</Components.Anchor>
          <Components.Button type="button" onClick={()=>{handleLogin()}}>Sign In</Components.Button>
        </Components.Form>
      </Components.SignInContainer>
      <Components.OverlayContainer signingIn={signIn}>
        <Components.Overlay signingIn={signIn}>
          <Components.LeftOverlayPanel signingIn={signIn}>
            <Components.Title>Welcome Back!</Components.Title>
            <Components.Paragraph>
              To keep connected with us please login with your personal info
            </Components.Paragraph>
            <Components.GhostButton onClick={() => toggle(true)}>
              Sign In
            </Components.GhostButton>
          </Components.LeftOverlayPanel>
          <Components.RightOverlayPanel signingIn={signIn}>
            <Components.Title>Hello, Friend!</Components.Title>
            <Components.Paragraph>
              Enter your personal details and start journey with us
            </Components.Paragraph>
            <Components.GhostButton onClick={() => toggle(false)}>
              Sign Up
            </Components.GhostButton>
          </Components.RightOverlayPanel>
        </Components.Overlay>
      </Components.OverlayContainer>
    </Components.Container>
  );
}

