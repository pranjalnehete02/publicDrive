import React, { useEffect } from "react";
import { Box, Typography, CssBaseline, AppBar, Toolbar } from "@mui/material";
import { FileManager, Sidebar } from "./FileManager";
import axios from "axios";
import { useDispatch } from 'react-redux'
import {addFilenames} from '../reduxstore/counterSlice'
import { Outlet } from "react-router-dom";


const HomePage = () => {
  const dispatcher = useDispatch();
  useEffect(() => {
    console.log("working 1 ");
    const fetchFiles = async () => {
      console.log("working ");
      try {
        console.log("working ");
        const response = await axios.get(
          "http://localhost:8080/api/files/files"
        );
        console.log(response.data)
        dispatcher(addFilenames(response.data));
      } catch (error) {
        console.error("Error fetching files", error);
      }
    };
    fetchFiles();
  }, []);

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
          <Outlet/>
        </Box>
      </Box>
    );
  };
  
  export default HomePage;