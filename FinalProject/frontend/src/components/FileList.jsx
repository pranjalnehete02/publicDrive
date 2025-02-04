import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
} from "@mui/material";
import { Download, Share, Pageview, Edit } from "@mui/icons-material";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom"; 
import toast from "react-hot-toast";

export const FileList = ({ files }) => {
    const username = useSelector(state=>state.user.user)
    const navigate = useNavigate();
    const editHandler = (fid, fname) => {
      const fileExtension = fname.split('.').pop().toLowerCase(); // Get file extension

      if (fileExtension !== "txt") {
          toast.error("Only text files (.txt) can be edited!", { position: "top-right" });
          return;
      }
      navigate(`editfile?fid=${fid}`);
  };
    const downloadHandler = async (fid) => {
        try {
          // Send GET request to backend to fetch file
          const response = await fetch(`http://localhost:8081/api/files/download/${fid}`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`, // Retrieve token from localStorage
            }
          });
          
      
          // Check for successful response
          if (!response.ok) {
            throw new Error('Failed to download file');
          }
      
          // Get the filename from the Content-Disposition header
          const contentDisposition = response.headers.get('Content-Disposition');
          let filename = 'downloaded-file';  // Default filename in case it's missing
      
          if (contentDisposition && contentDisposition.indexOf('attachment') !== -1) {
            const matches = /filename="([^"]*)"/.exec(contentDisposition);
            if (matches != null && matches[1]) {
              filename = matches[1];  // Extracted filename from the header
            }
          }
      
          // Get the file as a blob
          const blob = await response.blob();
      
          // Create a URL for the blob and trigger the download
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', filename);  // Use filename extracted from Content-Disposition header
          document.body.appendChild(link);
          link.click();
      
          // Clean up the link and revoke the URL
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
      
        } catch (error) {
          console.error('Error downloading the file:', error);
        }
      };
      
      const pageviewHandler = (fid)=>{
        navigate(`viewfile?fid=${fid}`)
      }
      
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
                View File
              </TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
                Owner
              </TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#34495e" }}>
                Actions
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>{
           
            }
            {files.map((file, index) => (
                
              <TableRow
                key={index}
                hover
                sx={{ "&:hover": { backgroundColor: "#d5dbdb" } }}
              >
                <TableCell>{file.fname}</TableCell>
                <TableCell>
                <IconButton onClick={()=>{pageviewHandler(file?.fid)}} size="small" sx={{ color: "#f39c12" }}>
                    <Pageview />
                </IconButton>
                </TableCell>
                <TableCell>{(file?.owner==username)?"me":file?.owner}</TableCell>
                <TableCell>
                  <IconButton onClick={()=>{downloadHandler(file?.fid)}} size="small" sx={{ color: "#3498db" }}>
                    <Download />
                  </IconButton>
                   <IconButton onClick={() => editHandler(file?.fid, file?.fname)} size="small" sx={{ color: "#3498db" }}>
                    <Edit />
                  </IconButton>
                  <IconButton onClick={()=>{navigate(`/homepage/shared?uname=${file?.owner}&fid=${file?.fid}`)}}  size="small" sx={{ color: "#f39c12" }}>
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