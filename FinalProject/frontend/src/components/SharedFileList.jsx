import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  CircularProgress,
  Box,
  Typography,
} from "@mui/material";
import { Download, Share, Pageview,Edit } from "@mui/icons-material";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios"; // Importing axios for API calls
import toast from "react-hot-toast";
export const SharedFileList = () => {
  const [files, setFiles] = useState([]); // State to store files
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState(""); // Error state
  const username = useSelector((state) => state.user.user); // Retrieve username from Redux
  const navigate = useNavigate(); // Navigate for pageview
  
  useEffect(() => {
    // Fetch shared files when the component is mounted
    const fetchSharedFiles = async () => {
      try {
        const response = await axios.get(
          `${process.env.REACT_APP_API_URL}/api/share/files/shared-files?username=${username}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`, // Use token from localStorage
            },
          }
        );

        // Set the files in the state
        setFiles(response.data);
        setLoading(false); // Stop loading after data is fetched
      } catch (error) {
        setError("Failed to fetch shared files.");
        setLoading(false);
      }
    };

    fetchSharedFiles(); // Call the function to fetch files
  }, [username]); // Dependency array to refetch files if username changes
  const editHandler = (fid, fname) => {
    const fileExtension = fname.split('.').pop().toLowerCase(); // Get file extension

    if (fileExtension !== "txt") {
        toast.error("Only text files (.txt) can be edited!", { position: "top-right" });
        return;
    }
    navigate(`/homepage/editfile?fid=${fid}`);
};
  const downloadHandler = async (fid) => {
    try {
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/api/files/download/${fid}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`, // Retrieve token from localStorage
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to download file");
      }

      const contentDisposition = response.headers.get("Content-Disposition");
      let filename = "downloaded-file"; // Default filename

      if (
        contentDisposition &&
        contentDisposition.indexOf("attachment") !== -1
      ) {
        const matches = /filename="([^"]*)"/.exec(contentDisposition);
        if (matches != null && matches[1]) {
          filename = matches[1]; 
        }
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename); 
      document.body.appendChild(link);
      link.click();

      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading the file:", error);
    }
  };

  const pageviewHandler = (fid) => {
    navigate(`/homepage/viewfile?fid=${fid}`);
  };

  if (loading) {
    return <CircularProgress />;
  }

  if (error) {
    return <div>{error}</div>; 
  }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h6" sx={{ marginBottom: 2 }}>
        Shared Files
      </Typography>
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
                View File
              </TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
                Owner
              </TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
                Permission
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
                <TableCell>{file.fileName}</TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => {
                      pageviewHandler(file.fileId);
                    }}
                    size="small"
                    sx={{ color: "#f39c12" }}
                  >
                    <Pageview />
                  </IconButton>
                </TableCell>
                <TableCell>{file.ownerOfFile}</TableCell>
                <TableCell>{file.permission}</TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => {
                      if(file.permission === "READ"){

                      }
                      else{
                      downloadHandler(file.fileId);
                      }
                    }}
                    size="small"
                    sx={{ color: "#3498db" }}
                  >
                    <Download />
                  </IconButton>
                   <IconButton onClick={() => editHandler(file?.fileId, file?.fileName)} size="small" sx={{ color: "#3498db" }}>
                                      <Edit />
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
    </Box>
  );
};
//sqa_09da34851daf63467a292e87033db82e82904ed