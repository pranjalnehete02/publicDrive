import { useState, useEffect } from "react";
import axios from "axios";
import './index.css';  // Or the name of your CSS file

const API_BASE_URL = "http://localhost:8080/files";

const App = () => {
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [fileContent, setFileContent] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [selectedFileType, setSelectedFileType] = useState("");
  const [textSize, setTextSize] = useState(16);  // Default font size

  useEffect(() => {
    fetchFiles();
  }, []);

  // Fetch files from backend
  const fetchFiles = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/list`);
      setFiles(response.data);
    } catch (error) {
      alert("Error fetching files");
    }
  };

  // Upload a new file
  const handleUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    let formData = new FormData();
    formData.append("file", file);

    try {
      await axios.post(`${API_BASE_URL}/upload`, formData);
      alert("File uploaded successfully!");
      fetchFiles();
    } catch (error) {
      alert("File upload failed!");
    }
  };

  // View file content
  const handleViewFile = async (filename) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/view/${filename}`);
      setSelectedFile(filename);
      setFileContent(response.data);
      setSelectedFileType(filename.endsWith(".html") ? "html" : "txt");
      setIsEditing(true);  // Always allow editing for both types
    } catch (error) {
      alert("Error viewing file");
    }
  };

  // Edit & Save file (For both HTML and text files)
  const handleSaveFile = async () => {
    if (!selectedFile) return;

    try {
      await axios.post(`${API_BASE_URL}/edit/${selectedFile}`, fileContent, {
        headers: { "Content-Type": "text/plain" },
      });
      alert("File saved successfully!");
      setIsEditing(false);
    } catch (error) {
      alert("Error saving file");
    }
  };

  // Download file
  const handleDownload = (filename) => {
    window.location.href = `${API_BASE_URL}/download/${filename}`;
  };

  // Delete file
  const handleDelete = async (filename) => {
    if (window.confirm(`Delete ${filename}?`)) {
      try {
        await axios.delete(`${API_BASE_URL}/delete/${filename}`);
        alert("File deleted successfully!");
        fetchFiles();
        setSelectedFile(null);
      } catch (error) {
        alert("Error deleting file");
      }
    }
  };

  // Increase text size
  const handleIncreaseTextSize = () => {
    setTextSize(prevSize => Math.min(prevSize + 2, 36));  // Max text size limit to 36px
  };

  return (
    <div className="max-w-7xl mx-auto p-6 bg-gray-50">
      <h1 className="text-4xl font-semibold text-center text-indigo-700 mb-10">File Manager</h1>

      {/* File Upload Section */}
      <div className="mb-6">
        <label htmlFor="fileUpload" className="block text-lg font-medium text-gray-700 mb-2">Upload a File</label>
        <input
          id="fileUpload"
          type="file"
          onChange={handleUpload}
          className="w-full p-3 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {/* File List Section */}
      <div className="mb-8">
        <h2 className="text-xl font-medium text-gray-800 mb-4">Uploaded Files</h2>
        <ul className="space-y-4">
          {files.map((file) => (
            <li key={file} className="flex justify-between items-center bg-white p-4 rounded-md shadow-md hover:bg-gray-50">
              <span className="text-lg text-gray-800">{file}</span>
              <div className="space-x-2">
                <button
                  onClick={() => handleViewFile(file)}
                  className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                >
                  View
                </button>
                <button
                  onClick={() => handleDownload(file)}
                  className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600"
                >
                  Download
                </button>
                <button
                  onClick={() => handleDelete(file)}
                  className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>

      {/* File Viewer / Editor Section */}
      {selectedFile && (
        <div className="mt-8 p-6 border border-gray-300 rounded-md shadow-md bg-white">
          <h2 className="text-2xl font-semibold mb-4">Editing: {selectedFile}</h2>
          <div className="flex items-center mb-4">
            <button
              onClick={handleIncreaseTextSize}
              className="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 mr-4"
            >
              Increase Text Size
            </button>
            <span className="text-sm text-gray-600">Current text size: {textSize}px</span>
          </div>
          {isEditing ? (
            <textarea
              className="w-full p-4 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={fileContent}
              rows={20}
              onChange={(e) => setFileContent(e.target.value)}
              style={{ fontSize: `${textSize}px` }}
            />
          ) : (
            <pre className="whitespace-pre-wrap" style={{ fontSize: `${textSize}px` }}>
              {fileContent}
            </pre>
          )}

          {isEditing && (
            <button
              onClick={handleSaveFile}
              className="mt-4 px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
            >
              Save Changes
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default App;
