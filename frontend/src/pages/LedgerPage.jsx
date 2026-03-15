import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getLedgerEntries, deleteLedgerEntry } from "../services/ledgerService";
import { getAccountById } from "../services/accountService";

function LedgerPage() {

  const { accountId } = useParams();
  const navigate = useNavigate();

  const [entries, setEntries] = useState([]);
  const [account, setAccount] = useState("null");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchLedger = async () => {

    try {

      setLoading(true);

      const [ledgerData, accountData] = await Promise.all([
        getLedgerEntries(accountId),
        getAccountById(accountId)
      ]);

      setEntries(ledgerData);
      setAccount(accountData)
      
      

    } catch (err) {

      setError("Failed to load ledger");

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {

    fetchLedger();

  }, [accountId]);

  const handleDelete = async (entryId) => {

    const confirmDelete = window.confirm(
      "Delete this entry?"
    );

    if (!confirmDelete) return;

    try {

      await deleteLedgerEntry(entryId);
      fetchLedger();

    } catch {

      alert("Delete failed");

    }

  };

  let runningBalance = 0;

  const totalCredit = entries
    .filter(e => e.type === "CREDIT")
    .reduce((sum, e) => sum + e.amount, 0);

  const totalDebit = entries
    .filter(e => e.type === "DEBIT")
    .reduce((sum, e) => sum + e.amount, 0);

  const pending = totalDebit - totalCredit;

  if (loading) return <div className="p-6">Loading ledger...</div>;

  if (error) return <div className="p-6 text-red-500">{error}</div>;

  const isClosed = account.status === "CLOSED";
  
  

  return (
    <div className="max-w-5xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Account #{accountId}
      </h1>

      {/* Summary */}
      <div className="grid grid-cols-3 gap-4 mb-6">

        <div className="bg-white p-4 rounded-lg border">
          <p className="text-sm text-gray-500">Total Credit</p>
          <p className="text-lg font-semibold text-green-600">
            ₹{totalCredit}
          </p>
        </div>

        <div className="bg-white p-4 rounded-lg border">
          <p className="text-sm text-gray-500">Total Debit</p>
          <p className="text-lg font-semibold text-red-600">
            ₹{totalDebit}
          </p>
        </div>

        <div className="bg-white p-4 rounded-lg border">
          <p className="text-sm text-gray-500">Pending</p>
          <p className="text-lg font-semibold text-blue-600">
            ₹{pending}
          </p>
        </div>

      </div>

      {/* Header */}
      <div className="flex justify-between items-center mb-4">

        <h2 className="text-xl font-semibold">
          Ledger Entries
        </h2>

        <button
          disabled={isClosed}
          onClick={() =>
            navigate(`/account/${accountId}/add-entry`)
          }
          className={`px-4 py-2 rounded-lg text-white
          ${
            isClosed
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
          title={isClosed ? "Account closed. No more entries allowed." : ""}
        >
          + Add Entry
        </button>

      </div>

      {/* Table */}
      <div className="bg-white border rounded-xl shadow-sm overflow-hidden">

        <div className="grid grid-cols-6 bg-gray-100 p-4 text-sm font-semibold">
          <span>Date</span>
          <span>Description</span>
          <span>Amount</span>
          <span>Balance</span>
          <span>Edit</span>
          <span>Delete</span>
        </div>

        {entries.length === 0 && (
          <p className="p-4 text-gray-500">
            No ledger entries
          </p>
        )}

        {entries.map(entry => {

          if (entry.type === "DEBIT") {
            runningBalance += entry.amount;
          } else {
            runningBalance -= entry.amount;
          }

          return (
            <div
              key={entry.id}
              className="grid grid-cols-6 items-center border-t p-4 text-sm hover:bg-gray-50"
            >

              <span>
                {entry.createdDate?.split("T")[0]}
              </span>

              <span>{entry.description}</span>

              <span
                className={
                  entry.type === "CREDIT"
                    ? "text-green-600 font-semibold"
                    : "text-red-600 font-semibold"
                }
              >
                {entry.type === "CREDIT" ? "+ ₹" : "- ₹"}
                {entry.amount}
              </span>

              <span
                className={
                  runningBalance >= 0
                    ? "text-blue-600 font-semibold"
                    : "text-red-600 font-semibold"
                }
              >
                ₹{Math.abs(runningBalance)}
              </span>

              <button
                onClick={() =>
                  navigate(`/ledger/edit/${entry.id}`)
                }
                className="text-blue-600 hover:underline"
              >
                Edit
              </button>

              <button
                onClick={() =>
                  handleDelete(entry.id)
                }
                className="text-red-600 hover:underline"
              >
                Delete
              </button>

            </div>
          );

        })}

      </div>

    </div>
  );
}

export default LedgerPage;