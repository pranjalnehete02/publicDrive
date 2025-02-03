import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { TextField, Button, Box, Typography } from "@mui/material";
import toast from "react-hot-toast";

const EditFile = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const params = new URLSearchParams(location.search);
    const fid = params.get("fid");
    
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFileContent = async () => {
            try {
                const response = await fetch(`http://localhost:8081/api/v1/files/edit/${fid}`,
                    {
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + localStorage.getItem("token")
                            }
                    }
                );
                if (!response.ok) {
                    throw new Error("Failed to fetch file content");
                }
                const text = await response.text();
                setContent(text);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchFileContent();
    }, [fid]);

    const saveChanges = async () => {
        try {
            const response = await fetch(`http://localhost:8081/api/v1/files/edit/${fid}`, {
                method: "PUT",
                headers: { "Content-Type": "text/plain",Authorization: `Bearer ${localStorage.getItem("token")}`, },
                body: content,
            });

            if (!response.ok) {
                throw new Error("Failed to save file");
            }
            toast.success("file saved successfully")
            navigate("/homepage"); // Redirect after saving
        } catch (err) {
            toast.error(err.message);
        }
    };

    if (loading) return <Typography>Loading...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box sx={{ p: 2 }}>
            <Typography variant="h6">Editing File</Typography>
            <TextField
                multiline
                rows={10}
                fullWidth
                variant="outlined"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                sx={{ mt: 2 }}
            />
            <Box sx={{ mt: 2 }}>
                <Button variant="contained" onClick={saveChanges} sx={{ mr: 2 }}>Save</Button>
                <Button variant="outlined" onClick={() => navigate("/homepage")}>Cancel</Button>
            </Box>
        </Box>
    );
};

export default EditFile;
