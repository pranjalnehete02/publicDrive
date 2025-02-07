import React, { useState } from "react";
import axios from "axios";
import { Button, Card, Typography, Input, Box, CircularProgress, styled } from "@mui/material";
import { useSelector } from "react-redux";
import toast from 'react-hot-toast'

// Styled components for the UI
const Root = styled("div")(({ theme }) => ({
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  minHeight: "82vh",
  backgroundColor: "#fff",
}));

const StyledCard = styled(Card)(({ theme }) => ({
  maxWidth: "800px", // Increased the size of the card
  padding: theme.spacing(4),
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  boxShadow: theme.shadows[10],
  borderRadius: 8,
  backgroundColor: "#fff",
}));

const UploadPage = () => {
  const [file, setFile] = useState(null);
  const [folder, setFolder] = useState(null);
  const [isFileUpload, setIsFileUpload] = useState(true); // Track file/folder mode
  const [isUploading, setIsUploading] = useState(false);
  const username = useSelector(state=>state.user.user)

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleFolderChange = (e) => {
    setFolder(e.target.files); // Files for folder
  };

  const handleUpload = async () => {
    if (!file && !folder) return;

    const formData = new FormData();
    if (isFileUpload && file) {
      formData.append("file", file);
    } else if (!isFileUpload && folder) {
      Array.from(folder).forEach((file) => formData.append("files", file));
    }
    

    try {
      console.log(username)
      setIsUploading(true);
      const response = await axios.post("http://localhost:8081/api/files/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${localStorage.getItem("token")}` // Assuming token is stored in localStorage
        },
        params: {
          username: username
        }
      });      
      toast.success(response.data);
    } catch (error) {
      toast.error("Error uploading file");
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <Root>
      <StyledCard>
        <Typography variant="h5" gutterBottom>
          {isFileUpload ? "Upload File" : "Upload Folder"}
        </Typography>

        {isUploading && <CircularProgress />}

        
        {isFileUpload && (
          <Box>
            <Input
              type="file"
              fullWidth
              onChange={handleFileChange}
              inputProps={{ accept: "image/*, .pdf, .txt" }}
              sx={{ marginTop: 2, padding: "10px", fontSize: "16px" }}
            />
          </Box>
        )}

        {/* Folder Upload Section */}
        {!isFileUpload && (
          <Box>
            <Input
              type="file"
              multiple
              onChange={handleFolderChange}
              inputProps={{ webkitdirectory: "true", mozdirectory: "true" }}  // Properly adding folder attributes
              fullWidth
              sx={{ marginTop: 2, padding: "10px", fontSize: "16px" }}
            />
          </Box>
        )}

        <Button
          variant="contained"
          color="primary"
          onClick={handleUpload}
          disabled={isUploading}
          sx={{ marginTop: 3 }}
        >
          {isUploading ? "Uploading..." : "Upload"}
        </Button>

        <Button
          variant="outlined"
          onClick={() => setIsFileUpload(!isFileUpload)}
          sx={{ marginTop: 2 }}
        >
          Switch to {isFileUpload ? "Folder Upload" : "File Upload"}
        </Button>
      </StyledCard>
    </Root>
  );
};

export default UploadPage;
