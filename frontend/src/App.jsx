import AppRouter from "./routes/AppRouter";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function App() {
  return (
    <div className="bg-gray-100 min-h-screen">
      <AppRouter />
      <ToastContainer position="top-right" autoClose={2000} />
    </div>
  );
}

export default App;