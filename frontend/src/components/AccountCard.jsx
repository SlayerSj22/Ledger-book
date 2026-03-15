import { useNavigate } from "react-router-dom";
import { deleteAccount } from "../services/accountService";

function AccountCard({ account, refresh }) {

  const navigate = useNavigate();

  const isClosed = account.status === "CLOSED";

  const handleDelete = async () => {

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this account?"
    );

    if (!confirmDelete) return;

    try {

      await deleteAccount(account.id);

      if (refresh) refresh();

    } catch (err) {

      console.error("Delete failed", err);
      alert("Failed to delete account");

    }

  };

  return (
    <div className="grid grid-cols-5 gap-4 items-center border rounded-lg p-4 hover:bg-gray-50">

      {/* Customer */}
      <div className="font-semibold capitalize">
        {account.partyName}
        <div className="text-xs text-gray-500">
          {account.description || "No description"}
        </div>
      </div>

      {/* Total */}
      <div>
        ₹{Number(account.totalBill).toLocaleString()}
      </div>

      {/* Pending */}
      <div className="text-red-600 font-semibold">
        ₹{Number(account.pendingAmount).toLocaleString()}
      </div>

      {/* Status */}
      <div>
        <span
          className={`px-2 py-1 text-xs rounded-full ${
            account.status === "OPEN"
              ? "bg-yellow-100 text-yellow-700"
              : "bg-green-100 text-green-700"
          }`}
        >
          {account.status}
        </span>
      </div>

      {/* Actions */}
      <div className="flex gap-2 justify-end">

        <button
          disabled={isClosed}
          onClick={() =>
            navigate(`/account/${account.id}/add-entry?type=CREDIT`)
          }
          className={`px-3 py-1 rounded text-white text-sm
          ${isClosed
            ? "bg-gray-300 cursor-not-allowed"
            : "bg-green-600 hover:bg-green-700"}`}
        >
          + Credit
        </button>

        <button
          disabled={isClosed}
          onClick={() =>
            navigate(`/account/${account.id}/add-entry?type=DEBIT`)
          }
          className={`px-3 py-1 rounded text-white text-sm
          ${isClosed
            ? "bg-gray-300 cursor-not-allowed"
            : "bg-red-600 hover:bg-red-700"}`}
        >
          + Debit
        </button>

        <button
          onClick={() => navigate(`/account/${account.id}`)}
          className="border px-3 py-1 rounded text-sm hover:bg-gray-100"
        >
          Ledger
        </button>

        <button
          onClick={handleDelete}
          className="border px-3 py-1 rounded text-sm text-red-600 hover:bg-gray-100"
        >
          Delete
        </button>

      </div>

    </div>
  );
}

export default AccountCard;