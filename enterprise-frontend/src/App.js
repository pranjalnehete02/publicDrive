import { BrowserRouter as Router, Routes, Route, Outlet } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import HomePage from './components/HomePage';
import { FileManager } from './components/FileManager';
import UploadPage from './components/UploadPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/upload" element={<UploadPage />} />
        <Route path="/" element={<Login />} /> {/* Default to login */}
        {/* <Route path='homepage' element={<HomePage/>} /> */}

        <Route path="/homepage" element={<HomePage />}>
          <Route index element={<FileManager />} />
          <Route path="upload" element={<UploadPage />} /> 
        </Route>
      </Routes>
    </Router>
  );
}

export default App;