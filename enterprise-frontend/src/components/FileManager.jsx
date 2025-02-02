import React, { useState } from "react";
import {
  Container,
  TextField,
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  Drawer,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Divider,
  ListItemButton,
} from "@mui/material";
import { Download, Share } from "@mui/icons-material";
import { ExitToApp } from "@mui/icons-material";
import { useSelector } from "react-redux";
import { Link } from 'react-router-dom';

export const Sidebar = () => {
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
            primary="Logout"
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

export const FileList = ({ files }) => {
  return (
    <TableContainer
      component={Paper}
      elevation={2}
      sx={{ borderRadius: 2, backgroundColor: "#ecf0f1" }}
    >
      <Table>
        <TableHead>
          <TableRow>
            <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
              File Name
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
              Size
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
              Owner
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
              Actions
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {files.map((file, index) => (
            <TableRow
              key={index}
              hover
              sx={{ "&:hover": { backgroundColor: "#d5dbdb" } }}
            >
              <TableCell>{file}</TableCell>
              <TableCell>{file?.size}</TableCell>
              <TableCell>{file?.owner}</TableCell>
              <TableCell>
                <IconButton size="small" sx={{ color: "#3498db" }}>
                  <Download />
                </IconButton>
                <IconButton size="small" sx={{ color: "#f39c12" }}>
                  <Share />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export const FileManager = () => {
  const [searchTerm, setSearchTerm] = useState("");
  //   const[files, setFiles] = useState([]);
  let files = useSelector((state) => state.counter.filenames);
  console.log("from filemanager: ", files);
  
  //   const [files, setFiles] = useState([
  //     { id: 1, name: "Document.pdf", type: "pdf", size: "1.2MB", owner: "Me" },
  //     { id: 2, name: "Project.zip", type: "zip", size: "15MB", owner: "Me" },
  //     { id: 3, name: "Image.png", type: "image", size: "2.5MB", owner: "Me" },
  //     { id: 4, name: "Notes.txt", type: "text", size: "500KB", owner: "Me" },
  //     { id: 5, name: "Douument.pdf", type: "pdf", size: "1.2MB", owner: "Me" },
  //   ]);

  //   const filteredFiles = files.filter((file) =>
  //     file.name.toLowerCase().includes(searchTerm.toLowerCase())
  //   );

  const filteredFiles = files.filter((file) =>
    file.toLowerCase().includes(searchTerm.toLowerCase())
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
