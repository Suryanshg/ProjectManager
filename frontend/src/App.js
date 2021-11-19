import { HashRouter, Routes, Route } from "react-router-dom";

import Admin from "./pages/Admin";
import ExamplePage from "./pages/ExamplePage";
import NotFound from "./pages/NotFound";

function App() {
  /* Add site wide css here */
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<ExamplePage />} />
        <Route path="project/:projectId" element={<Admin />} />
        <Route path="admin" element={<Admin />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </HashRouter>
  );
}

export default App;
