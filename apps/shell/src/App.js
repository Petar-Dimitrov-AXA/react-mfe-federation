import { jsx as _jsx, jsxs as _jsxs, Fragment as _Fragment } from "react/jsx-runtime";
import { Suspense } from 'react';
import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import './App.css';
import React from 'react';
import { Sidebar } from '@react-mfe-federation/shared-ui';
function App() {
    const App1 = React.lazy(() => import('app1/App'));
    const App2 = React.lazy(() => import('app2/App'));
    const App3 = React.lazy(() => import('app3/App'));
    return (_jsx(_Fragment, { children: _jsxs(BrowserRouter, { children: [_jsxs("nav", { children: [_jsx(Link, { to: "/app1", children: "App 1" }), _jsx(Link, { to: "/app2", children: "App 2" }), _jsx(Link, { to: "/app3", children: "App 3" })] }), _jsx(Sidebar, { list: ['App 1', 'App 2', 'App 3'] }), _jsx(Suspense, { fallback: _jsx("div", { children: "Loading..." }), children: _jsxs(Routes, { children: [_jsx(Route, { path: "/app1/*", element: _jsx(App1, {}) }), _jsx(Route, { path: "/app2/*", element: _jsx(App2, {}) }), _jsx(Route, { path: "/app3/*", element: _jsx(App3, {}) })] }) })] }) }));
}
export default App;
