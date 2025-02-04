import React, { useState } from "react";
import {
  Container,
  TextField,
  Box,
  Typography,
  List,
  ListItemText,
  Drawer,
  IconButton,
  Divider,
  ListItemButton,
} from "@mui/material";
import { ExitToApp } from "@mui/icons-material";
import { useDispatch, useSelector } from "react-redux";
import { Link } from 'react-router-dom';
import { FileList } from "./FileList";
import { deleteUser } from "../reduxstore/userSlice";
import {removeFilenames} from "../reduxstore/usersFileSlice"


export const Sidebar = () => {
  const dispatch = useDispatch();

  return (
    <Drawer
      variant="permanent"
      sx={{ width: 240, flexShrink: 0, backgroundColor: "#2c3e50" }}
    >
      <Box
        sx={{
          width: 240,
          p: 3,
          color: "white",
          display: "flex",
          flexDirection: "column",
          height: "100%",
        }}
      >
        <Typography variant="h6" fontWeight={600} color="primary.main">
          Navigation
        </Typography>
        <Divider sx={{ my: 2, borderColor: "rgba(255, 255, 255, 0.3)" }} />

        {/* Navigation List */}
        <List>
          <ListItemButton
            sx={{
              backgroundColor: "#1989df87",
              "&:hover": { backgroundColor: "#1976d2" },
              borderRadius: "4px",
              mb: 1,
            }}
            component={Link} 
            to="/homepage" 
          >
            <ListItemText primary="My Drive" sx={{ color: "white" }} />
          </ListItemButton>
          <ListItemButton
            sx={{
              backgroundColor: "#1989df87",
              "&:hover": { backgroundColor: "#1976d2" },
              borderRadius: "4px",
              mb: 1,
            }}
            component={Link}
            to="/homepage/upload"
          >
            <ListItemText primary="Upload File & Folder" sx={{ color: "white" }} />
          </ListItemButton>
          <ListItemButton
            sx={{
              backgroundColor: "#1989df87",
              "&:hover": { backgroundColor: "#1976d2" },
              borderRadius: "4px",
              mb: 1,
            }}
            component={Link}
            to="/homepage/sharedwithme"
          >
            <ListItemText primary="Shared with me" sx={{ color: "white" }} />
          </ListItemButton>
          <ListItemButton
            sx={{
              backgroundColor: "#1989df87",
              "&:hover": { backgroundColor: "#1976d2" },
              borderRadius: "4px",
              mb: 1,
            }}
          >
            <ListItemText primary="Recent" sx={{ color: "white" }} />
          </ListItemButton>
        </List>

        <Box sx={{ flexGrow: 6 }} />

        <ListItemButton
          sx={{
            backgroundColor: "#e74c3c",
            "&:hover": { backgroundColor: "#c0392b" },
            borderRadius: "4px",
            mt: 2,
            height: "1px",
            padding: "0 16px",
          }}
        >
          <ListItemText
            primary="Logout" onClick={()=>{dispatch(deleteUser()); dispatch(removeFilenames()); console.log("logout");}}
            sx={{ color: "white", marginRight: 2 }}
          />
          <IconButton sx={{ color: "white" }}>
            <ExitToApp />
          </IconButton>
        </ListItemButton>
      </Box>
    </Drawer>
  );
};

export const FileManager = () => {
  const [searchTerm, setSearchTerm] = useState("");
  let files = useSelector((state) => state.usersfile.filenames);
  console.log("from filemanager: ", files);

  const filteredFiles = files.filter((file) =>
    file.fname.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container
      sx={{ mt: 3, flexGrow: 1, backgroundColor: "#f7f7f7", borderRadius: 2 }}
    >
      <TextField
        fullWidth
        label="Search in Drive"
        variant="outlined"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        sx={{
          mb: 2,
          backgroundColor: "white",
          borderRadius: 1,
          "& .MuiInputBase-root": {
            borderColor: "#bdc3c7",
          },
        }}
      />
      <Typography variant="h6" gutterBottom fontWeight={600} color="#34495e">
        My Drive
      </Typography>
      <FileList files={filteredFiles} />
    </Container>
  );
};
