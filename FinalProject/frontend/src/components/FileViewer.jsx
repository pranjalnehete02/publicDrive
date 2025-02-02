import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom'; // Import useLocation from react-router-dom

const FileViewer = () => {
  const [fileContent, setFileContent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [contentType, setContentType] = useState(null);
  
  const location = useLocation(); // Get the current URL location

  // Extract fid from the query parameters
  const getFidFromQuery = () => {
    const params = new URLSearchParams(location.search); // Parse the query string
    return params.get('fid'); // Get the 'fid' parameter from the query string
  };

  const fid = getFidFromQuery(); // Get fid from the query string

  useEffect(() => {
    if (!fid) {
      setError('File ID (fid) is missing in the query string.');
      setLoading(false);
      return;
    }

    const fetchFileContent = async () => {
      try {
        const response = await fetch(`http://localhost:8081/api/files/download/${fid}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`, // Assuming token is stored in localStorage
          }
        });
        

        if (!response.ok) {
          throw new Error('Failed to fetch file');
        }

        const blob = await response.blob();
        const contentType = response.headers.get('Content-Type');
        setContentType(contentType);

        if (contentType.includes('application/pdf')) {
          // Display PDF if the file is a PDF
          setFileContent(URL.createObjectURL(blob));
        } else if (contentType.includes('image')) {
          // Display image if the file is an image
          setFileContent(URL.createObjectURL(blob));
        } else if (contentType.includes('text')) {
          // Display text if the file is a text file
          const text = await blob.text();
          setFileContent(text);
        } else {
          // Handle other file types, if necessary (e.g., binary files)
          setFileContent('Unsupported file type');
        }

        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchFileContent();
  }, [fid]); // Re-run if fid changes

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (fileContent) {
    if (contentType.includes('application/pdf')) {
      // Render PDF
      return (
        <div>
          <object
            data={fileContent}
            type="application/pdf"
            width="100%"
            height="600px"
          >
            <p>Your browser does not support PDFs.</p>
          </object>
        </div>
      );
    } else if (contentType.includes('image')) {
      // Render image
      return (
        <div>
          <img
            src={fileContent}
            alt="File Content"
            style={{ width: '100%', height: 'auto' }}
          />
        </div>
      );
    } else if (contentType.includes('text')) {
      // Render text content (e.g., for .txt files)
      return <pre>{fileContent}</pre>;
    } else {
      return <div>{fileContent}</div>;
    }
  }

  return null;
};

export default FileViewer;
