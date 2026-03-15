import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getPartyById } from "../services/partyService";
import { getAccountsByParty } from "../services/accountService";
import AccountCard from "../components/AccountCard";

function PartyPage() {

  const { id } = useParams();
  const navigate = useNavigate();

  const [party, setParty] = useState(null);
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {

    try {

      setLoading(true);

      const partyData = await getPartyById(id);
      setParty(partyData);

      const accountsData = await getAccountsByParty(id);
      setAccounts(accountsData);

    } catch (err) {

      console.error("Failed to fetch party data", err);

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {
    fetchData();
  }, [id]);

  if (loading) {
    return <div className="p-6">Loading...</div>;
  }

  if (!party) {
    return <div className="p-6">Party not found</div>;
  }

  const totalPending = accounts.reduce(
    (sum, acc) => sum + Number(acc.pendingAmount || 0),
    0
  );

  return (
    <div className="max-w-6xl mx-auto p-6">

      {/* Party Info */}
      <div className="bg-white rounded-xl shadow-sm border p-6 mb-6">

        <h1 className="text-2xl font-bold capitalize">
          {party.name}
        </h1>

        <p className="text-gray-600">
          S/O {party.fatherName}
        </p>

        <p className="text-gray-600">
          Village: {party.village}
        </p>

        <p className="text-gray-600">
          Phone: {party.phone}
        </p>

        <p className="mt-3 font-semibold text-red-600">
          Total Pending: ₹{totalPending.toLocaleString()}
        </p>

      </div>


      {/* Accounts Header */}
      <div className="flex justify-between items-center mb-4">

        <h2 className="text-xl font-semibold">
          Accounts
        </h2>

        <button
          onClick={() => navigate(`/party/${id}/create-account`)}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          + Create Account
        </button>

      </div>


      {/* Accounts Table */}
      <div className="bg-white border rounded-xl shadow-sm">

        {/* Sticky Header */}
        <div className="grid grid-cols-5 gap-4 p-4 text-sm font-semibold text-gray-600 border-b bg-gray-50 sticky top-0 z-10">

          <span>Customer</span>
          <span>Total</span>
          <span>Pending</span>
          <span>Status</span>
          <span className="text-right">Actions</span>

        </div>


        {/* Scrollable Body */}
        <div className="max-h-[420px] overflow-y-auto">

          {accounts.length === 0 && (
            <p className="p-6 text-gray-500">
              No accounts found for this party
            </p>
          )}

          <div className="flex flex-col gap-3 p-3">

            {accounts.map((account) => (
              <AccountCard
                key={account.id}
                account={account}
                refresh={fetchData}
              />
            ))}

          </div>

        </div>

      </div>

    </div>
  );
}

export default PartyPage;