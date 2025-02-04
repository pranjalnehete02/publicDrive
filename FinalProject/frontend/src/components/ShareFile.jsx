import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, MenuItem, Select, InputLabel, FormControl, Box, Typography } from '@mui/material';
import { useLocation } from 'react-router-dom';
import toast from 'react-hot-toast';

const ShareFile = () => {
  const [sharedToUser, setSharedToUser] = useState('');
  const [permission, setPermission] = useState('READ');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const location = useLocation(); 

  
  const getFileId = () => {
    const params = new URLSearchParams(location.search); 
    return params.get('fid');
  };
  const getSenderUsername = () =>{
    const params = new URLSearchParams(location.search);
    return params.get('uname')
  }
  const fileId = getFileId();
  const sharedByUsername = getSenderUsername();
  const handleSubmit = async (event) => {
    event.preventDefault();
    
    try {
      const requestData = {
        fileId,
        sharedByUsername,  
        sharedToUser,
        permission
      };
      console.log(requestData);

      const response = await axios.post(`${process.env.REACT_APP_API_URL}/api/share/files/share`, requestData);
      
      setMessage('File shared successfully!');
      toast.success("File shared successfully!")
      setError('');
    } catch (err) {
      setError(err.response?.data || 'Something went wrong!');
      toast.error("Fail to share!")
      setMessage('');
    }
  };

  return (
    <Box sx={{ maxWidth: 400, margin: 'auto', padding: 2 }}>
      <Typography variant="h5" gutterBottom>
        Share File
      </Typography>

      <form onSubmit={handleSubmit}>
              
        <TextField
          label="Shared To Username"
          fullWidth
          required
          value={sharedToUser}
          onChange={(e) => setSharedToUser(e.target.value)}
          sx={{ marginBottom: 2 }}
        />

        <FormControl fullWidth sx={{ marginBottom: 2 }}>
          <InputLabel>Permission</InputLabel>
          <Select
            value={permission}
            label="Permission"
            onChange={(e) => setPermission(e.target.value)}
          >
            <MenuItem value="READ">READ</MenuItem>
            <MenuItem value="WRITE">WRITE</MenuItem>
          </Select>
        </FormControl>

        {message && <Typography color="green">{message}</Typography>}
        {error && <Typography color="error">{error}</Typography>}

        <Button type="submit" variant="contained" color="primary" fullWidth>
          Share File
        </Button>
      </form>
    </Box>
  );
};

export default ShareFile;