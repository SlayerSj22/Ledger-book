import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getLedgerEntry, updateLedgerEntry } from "../services/ledgerService";

function EditEntryPage() {

  const { entryId } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    amount: "",
    description: "",
    type: "DEBIT"
  });

  const [error, setError] = useState("");

  useEffect(() => {

    const fetchEntry = async () => {

      try {

        const data = await getLedgerEntry(entryId);

        setFormData({
          amount: data.amount,
          description: data.description,
          type: data.type
        });

      } catch {

        setError("Failed to load entry");

      }

    };

    fetchEntry();

  }, [entryId]);

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });

    setError("");

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    if (!formData.amount || formData.amount <= 0) {
      setError("Amount must be greater than 0");
      return;
    }

    try {

      await updateLedgerEntry(entryId, {
        amount: Number(formData.amount),
        description: formData.description,
        type: formData.type
      });

      navigate(-1);

    } catch (err) {

      const message =
        err?.response?.data?.message ||
        "Update failed";

      setError(message);

    }

  };

  return (
    <div className="max-w-xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Edit Ledger Entry
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-xl shadow space-y-4"
      >

        <select
          name="type"
          value={formData.type}
          onChange={handleChange}
          className="w-full border px-3 py-2 rounded-lg"
        >
          <option value="DEBIT">Debit</option>
          <option value="CREDIT">Credit</option>
        </select>

        <input
          type="number"
          name="amount"
          value={formData.amount}
          onChange={handleChange}
          className="w-full border px-3 py-2 rounded-lg"
        />

        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          className="w-full border px-3 py-2 rounded-lg"
        />

        {error && (
          <div className="bg-red-100 text-red-700 p-3 rounded">
            {error}
          </div>
        )}

        <div className="flex gap-3">

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg"
          >
            Update
          </button>

          <button
            type="button"
            onClick={() => navigate(-1)}
            className="border px-4 py-2 rounded-lg"
          >
            Cancel
          </button>

        </div>

      </form>

    </div>
  );
}

export default EditEntryPage;