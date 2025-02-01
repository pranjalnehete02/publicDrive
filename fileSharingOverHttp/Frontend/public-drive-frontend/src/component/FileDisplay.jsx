import React, { useEffect, useState } from "react";
import axios from "axios";
import FileCard from "./FileCard";

const FileDisplay = () => {
  const [files, setFiles] = useState([]);

  useEffect(() => {
    console.log("working 1 ");
    const fetchFiles = async () => {
      console.log("working ");
      try {
        console.log("working ");
        const response = await axios.get(
          "http://192.168.100.120:8080/api/files/files"
        );
        console.log("glkjfjlsdkjl", response.data);
        setFiles(response.data);
      } catch (error) {
        console.error("Error fetching files", error);
      }
    };
    fetchFiles();
  }, []);

  return (
    <div>
      <h3 className="text-red-200">Uploaded Files:</h3>

      <div className="flex justify-around flex-wrap">
        {files.map((file) => (
          <FileCard key={file.id} filename={file} />
          // <a href={`http://192.168.100.120:8080/api/files/download/${file}`} target="_blank" rel="noopener noreferrer">
          //     {file}
          // </a>
        ))}
      </div>
    </div>
  );
};

export default FileDisplay;
