import React, { useEffect, useState } from "react";
import { Box, Typography, CssBaseline, AppBar, Toolbar } from "@mui/material";
import { FileManager, Sidebar } from "./FileManager";
import axios from "axios";
import { useDispatch, useSelector } from 'react-redux';
import { addFilenames } from '../reduxstore/usersFileSlice';
import { Outlet, useNavigate } from "react-router-dom";

const HomePage = () => {
  const [loading, setLoading] = useState(true);
  const dispatcher = useDispatch();
  const userinfo = useSelector(state => state.user.user);
  const username = useSelector(state=>state.user.user)
  const navigate = useNavigate();

  // Fetch files
  useEffect(() => {
    const fetchFiles = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.post(
          'http://localhost:8081/api/files/files',
          null,
          {
            params: { username },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response.data)
        
        dispatcher(addFilenames(response.data));
      } catch (error) {
        console.error("Error fetching files", error);
      }
      setLoading(false); // After fetching data, stop loading
    };
    fetchFiles();
  }, [dispatcher]);

  // Redirect if userinfo is null
  useEffect(() => {
    if (userinfo === null) {
      navigate('/');
    }
  }, [userinfo, navigate]);

  if (loading) {
    return <div>Loading...</div>; 
  }

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      <Sidebar />
      <Box sx={{ flexGrow: 1, p: 3 }}>
        <AppBar position="static" elevation={1} sx={{ backgroundColor: "#f5f5f5", color: "black" }}>
          <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1 }} fontWeight={600}>
              Public Drive
            </Typography>
          </Toolbar>
        </AppBar>
        <Outlet />
      </Box>
    </Box>
  );
};

export default HomePage;
