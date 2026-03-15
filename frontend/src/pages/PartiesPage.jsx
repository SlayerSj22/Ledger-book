import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllParties, deleteParty } from "../services/partyService";

function PartiesPage() {

  const [parties, setParties] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  const fetchParties = async () => {

    try {

      const data = await getAllParties();
      setParties(data);

    } catch (err) {

      console.error("Failed to fetch parties", err);

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {
    fetchParties();
  }, []);

  const handleDelete = async (id) => {

  if (!window.confirm("Are you sure?")) return;

  try {

    await deleteParty(id);

    await fetchParties();   // refresh from server

  } catch (err) {

    alert(err?.response?.data?.message || "Delete failed");

  }

};
  if (loading) {
    return <div className="p-6">Loading parties...</div>;
  }

  return (
    <div className="max-w-6xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        All Parties
      </h1>

      <div className="bg-white shadow rounded-xl overflow-hidden">

        <table className="w-full">

          <thead className="bg-gray-100">

            <tr>

              <th className="text-left p-3">Name</th>
              <th className="text-left p-3">Father Name</th>
              <th className="text-left p-3">Village</th>
              <th className="text-left p-3">Phone</th>
              <th className="text-left p-3">Actions</th>

            </tr>

          </thead>

          <tbody>

            {parties.map((party) => (

              <tr key={party.id} className="border-t">

                <td className="p-3 font-medium">
                  {party.name}
                </td>

                <td className="p-3">
                  {party.fatherName || "-"}
                </td>

                <td className="p-3">
                  {party.village || "-"}
                </td>

                <td className="p-3">
                  {party.phone || "-"}
                </td>

                <td className="p-3 flex gap-3">

                  <button
                    onClick={() => navigate(`/party/${party.id}`)}
                    className="text-blue-600 hover:underline"
                  >
                    View
                  </button>

                  <button
                    onClick={() => handleDelete(party.id)}
                    className="text-red-600 hover:underline"
                  >
                    Delete
                  </button>

                </td>

              </tr>

            ))}

            {parties.length === 0 && (

              <tr>

                <td colSpan="5" className="text-center p-6 text-gray-500">
                  No parties found
                </td>

              </tr>

            )}

          </tbody>

        </table>

      </div>

    </div>
  );
}

export default PartiesPage;