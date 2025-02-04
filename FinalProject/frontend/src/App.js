import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import HomePage from './components/HomePage';
import { FileManager } from './components/FileManager';
import UploadPage from './components/UploadPage';
import {Toaster } from 'react-hot-toast'
import FileViewer from './components/FileViewer';
import EditFile from './components/EditFile';
import ForgotPassword from './components/Auth/ForgotPassword';
import ResetPassword from './components/Auth/ResetPassword';
import ShareFile from './components/ShareFile';
import { SharedFileList } from './components/SharedFileList';

function App() {
  return (
    <Router>
      <Toaster/>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/upload" element={<UploadPage />} />
        <Route path="/forgotpassword" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/" element={<Login />} /> {/* Default to login */}
        {/* <Route path='homepage' element={<HomePage/>} /> */}

        <Route path="/homepage" element={<HomePage />}>
          <Route index element={<FileManager />} />
          <Route path="editfile" element={<EditFile />} />
          
          <Route path="upload" element={<UploadPage />} />
          <Route path="viewfile" element={<FileViewer />}/> 
          
          <Route path="sharedwithme" element={<SharedFileList/>} />
          <Route path="shared" element={<ShareFile/>} />
        </Route>
        
      </Routes>
    </Router>
  );
}

export default App;