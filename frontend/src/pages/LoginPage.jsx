import { useState , useEffect} from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function LoginPage() {

  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const navigate = useNavigate();

  useEffect(() => {

  const token = localStorage.getItem("token");

  if (token) {
    navigate("/");
  }

}, [navigate]);

  const handleLogin = async (e) => {

    e.preventDefault();

    try {

      const res = await axios.post(
  "http://localhost:8000/auth/login",
  { email, password }
);

      

      localStorage.setItem("token",res.data.token);

      toast.success("Welcome back!");

      navigate("/");

    } catch {

      toast.error("Invalid email or password");

    }

  };

  return (
    <div className="h-screen flex">

      {/* LEFT SIDE - LOGIN */}
      <div className="w-1/2 flex items-center justify-center bg-white">

        <form
          onSubmit={handleLogin}
          className="w-96 space-y-5"
        >

          <div>
            <h1 className="text-3xl font-bold text-gray-800">
              Welcome back
            </h1>

            <p className="text-gray-500 mt-1">
              Sign in to continue to LedgerBook
            </p>
          </div>

          <input
            type="email"
            placeholder="Email address"
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={email}
            onChange={(e)=>setEmail(e.target.value)}
          />

          <input
            type="password"
            placeholder="Password"
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={password}
            onChange={(e)=>setPassword(e.target.value)}
          />

          <button
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
          >
            Sign in
          </button>

          <p className="text-sm text-gray-500 text-center">
            Secure login powered by JWT authentication
          </p>

        </form>

      </div>

      {/* RIGHT SIDE - VISUAL */}
      <div className="w-1/2 bg-gradient-to-br from-blue-600 to-indigo-700 text-white flex items-center justify-center p-16">

        <div className="max-w-lg space-y-6">

          <h2 className="text-4xl font-bold leading-tight">
            Build clarity in your business finances.
          </h2>

          <p className="text-lg text-blue-100">
            LedgerBook helps you track accounts, manage payments,
            and understand outstanding balances — all in one place.
          </p>

          <div className="border-l-4 border-white pl-4 mt-8">
            <p className="italic text-blue-100">
              “Good accounting is the language of business.”
            </p>
            <p className="mt-2 text-sm">
              — Warren Buffett
            </p>
          </div>

        </div>

      </div>

    </div>
  );
}

export default LoginPage;