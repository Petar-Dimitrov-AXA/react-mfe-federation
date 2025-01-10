import { Suspense } from 'react'
import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import './App.css'
import React from 'react';
import { Sidebar } from '@react-mfe-federation/shared-ui';

function App() {
  const App1 = React.lazy(() => import('app1/App'));
  const App2 = React.lazy(() => import('app2/App'));
  const App3 = React.lazy(() => import('app3/App'));

  return (
    <>
    <BrowserRouter>
      <nav>
        <Link to="/app1">App 1</Link>
        <Link to="/app2">App 2</Link>
        <Link to="/app3">App 3</Link>
      </nav>

      <Sidebar list={['App 1', 'App 2', 'App 3']} />

      <Suspense fallback={<div>Loading...</div>}>
        <Routes>
          <Route path="/app1/*" element={<App1 />} />
          <Route path="/app2/*" element={<App2 />} />
          <Route path="/app3/*" element={<App3 />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
    </>
  )
}

export default App
