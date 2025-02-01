import React, { useState } from "react";
import axios from "axios";



const FileUpload = () => {
    const [file, setFile] = useState(null);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };
    const handleUpload = async () => {
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post("http://192.168.100.120:8080/api/files/upload", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            alert(response.data);
        } catch (error) {
            alert("Error uploading file");
        }
    };
    return (
        <div className="w-screen flex items-center justify-center ">
            <input type="file" className="file-input file-input-bordered file-input-primary w-full max-w-xs" onChange={handleFileChange} />
            <button onClick={handleUpload} className="btn btn-primary ml-11 px-10">Upload</button>
        </div>
    );
}

export default FileUpload
