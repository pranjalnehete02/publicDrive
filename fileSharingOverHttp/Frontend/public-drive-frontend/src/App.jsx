import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import FileUpload from './component/FileUpload'
import FileDisplay from './component/FileDisplay'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div className='p-10'>
        <FileUpload/>
        <FileDisplay/>
      </div>
    </>
  )
}

export default App
