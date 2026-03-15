import { useState, useEffect, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import { searchParties } from "../services/partyService";
import useDebounce from "../hooks/useDebounce";

function Navbar() {

  const [search, setSearch] = useState("");
  const [results, setResults] = useState([]);
  const [menuOpen, setMenuOpen] = useState(false);

  const navigate = useNavigate();
  const debouncedSearch = useDebounce(search, 300);

  const searchRef = useRef(null);
  const menuRef = useRef(null);

  // Fetch search results
  useEffect(() => {

    const fetchResults = async () => {

      if (debouncedSearch.length < 2) {
        setResults([]);
        return;
      }

      try {

        const data = await searchParties(debouncedSearch);
        setResults(data);

      } catch (err) {

        console.error("Search failed", err);

      }

    };

    fetchResults();

  }, [debouncedSearch]);

  // Close dropdown when clicking outside
  useEffect(() => {

    const handleClickOutside = (event) => {

      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setResults([]);
      }

      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setMenuOpen(false);
      }

    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };

  }, []);

  const handleSelect = (id) => {

    setSearch("");
    setResults([]);

    navigate(`/party/${id}`);

  };

  const handleLogout = () => {
  localStorage.removeItem("token");
  navigate("/login");
  };

  return (
    <nav className="bg-white border-b shadow-sm">

      <div className="max-w-7xl mx-auto px-6 py-3 flex items-center justify-between">

        {/* Logo */}
        <Link to="/" className="flex items-center gap-2 select-none">

          <div className="bg-blue-600 text-white px-3 py-1 rounded-lg font-bold">
            LB
          </div>

          <span className="font-semibold text-lg">
            LedgerBook
          </span>

        </Link>

        {/* Search */}
        <div ref={searchRef} className="relative w-96">

          <input
            type="text"
            placeholder="Search party..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full border px-4 py-2 rounded-lg"
          />

          {results.length > 0 && (

            <div className="absolute w-full bg-white border rounded-lg mt-1 shadow-md z-10">

              {results.map((party) => (

                <div
                  key={party.id}
                  onClick={() => handleSelect(party.id)}
                  className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                >

                  <div className="font-medium">
                    {party.name}
                  </div>

                  <div className="text-sm text-gray-500">
                    S/O {party.fatherName} • {party.village}
                  </div>

                </div>

              ))}

            </div>

          )}

        </div>

        {/* Right section */}
        <div className="flex items-center gap-3">

          {/* Add Party */}
          <button
            onClick={() => navigate("/party/add")}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg"
          >
            + Add Party
          </button>

          <div ref={menuRef} className="relative">

  <button
    onClick={() => setMenuOpen(!menuOpen)}
    className="p-2 border rounded-lg"
  >
    ☰
  </button>

  {menuOpen && (

    <div className="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-lg">

      <button
        onClick={() => {
          navigate("/parties");
          setMenuOpen(false);
        }}
        className="block w-full text-left px-4 py-2 hover:bg-gray-100"
      >
        View All Parties
      </button>

      <button
        onClick={handleLogout}
        className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-red-600"
      >
        Logout
      </button>

    </div>

  )}

</div>

        </div>

      </div>

    </nav>
  );
}

export default Navbar;