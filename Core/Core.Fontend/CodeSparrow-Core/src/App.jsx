  import { Routes, Route, Navigate } from "react-router-dom";
  import Login from "./Login";
  import Register from "./RegisterPageLayout";
  import Dashboard from "./Dashboard";
  import ProtectedRoute from "./components/ProtectedRoute";
  import Profile from "./Internals/profile";
  // import Profilesetting from "./Internals/profilesetting";
  import MainLayout from "./Layout/Layout";
  import InventoryPage from "./Inventory/InventoryPage";
  import NewSnippetPage from "./Inventory/new";
  import ViewUpdateSnippetPage from "./Inventory/ViewUpdateSnippetPage";
  import ConnectRouter from "./Connect/Connectrouter";
  
  function PublicRoute({ children }) {
  const token = localStorage.getItem("accessToken");
  if (token) return <Navigate to="/dashboard" replace />;
  return children;
  }
  function App() {
    return (
      <Routes>

        {/* public routes */}
        <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
        <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />

        {/* protected routes */}

        <Route
          element={
            <ProtectedRoute>
              <MainLayout />
            </ProtectedRoute>
          }
        >
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/internal/profile" element={<Profile />} />
        <Route path="/Inventory" element={<InventoryPage />} />
        <Route path="/new" element={<NewSnippetPage />} />
        <Route path="/view/:id" element={<ViewUpdateSnippetPage />} />
        <Route path="/community" element={<ConnectRouter />}/>
        {/* <Route 
          path="/internal/profilesetting"
          element={
            <ProtectedRoute>
              <Profilesetting />
            </ProtectedRoute>
          }
        /> */}
        </Route>
        {/* default */}
        <Route
          path="/"
          element={
            localStorage.getItem("accessToken")  // ← was "token", now "accessToken"
              ? <Navigate to="/dashboard" replace />
              : <Navigate to="/login" replace />
          }
        />
      </Routes>
    );
  }

  export default App;
