import { Route, Routes } from 'react-router-dom'
import './App.css'
import Layout from './components/Layout'
import ToastViewport from './components/ToastViewport'
import Homepage from './pages/Homepage'
import Books from './pages/Books'
import Authors from './pages/Authors'
import Record from './pages/Record'
import Borrowers from './pages/Borrowers'

function App() {
  return (
  <Layout>
    <ToastViewport />
    <Routes>
      <Route path="/" element={<Homepage />} />
      <Route path="/Authors" element={<Authors />} />
      <Route path="/Record" element={<Record/>} />
      <Route path='/Books' element={<Books />} />
      <Route path='/Borrowers' element={<Borrowers />} />
    </Routes>
  </Layout>
  )
}

export default App
