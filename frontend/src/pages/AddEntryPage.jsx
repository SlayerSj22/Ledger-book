import { useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { addLedgerEntry } from "../services/ledgerService";

function AddEntryPage() {

  const { accountId } = useParams();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const entryType = searchParams.get("type") || "CREDIT";

  const [formData, setFormData] = useState({
    type: entryType,
    amount: "",
    description: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");

  const validate = () => {

    const newErrors = {};

    if (!formData.amount) {
      newErrors.amount = "Amount is required";
    } else if (formData.amount <= 0) {
      newErrors.amount = "Amount must be greater than 0";
    }

    if (!formData.description.trim()) {
      newErrors.description = "Note is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

    setServerError("");
  };

  const handleSubmit = async (e) => {

  e.preventDefault();
  setServerError("");

  if (!validate()) return;

  const payload = {
    accountId: Number(accountId),
    amount: Number(formData.amount),
    type: formData.type,
    description: formData.description,
  };

  try {

    await addLedgerEntry(payload);

    // navigate only if request succeeded
    navigate(`/account/${accountId}`);

  } catch (err) {

    const message =
      err?.response?.data?.message ||
      "Failed to add ledger entry";

    setServerError(message);
  }
};

  return (
    <div className="max-w-xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Add Ledger Entry
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-xl shadow space-y-4"
      >

        {/* Entry Type */}
        <div>
          <label className="block mb-1 font-medium">
            Entry Type
          </label>

          <select
            name="type"
            value={formData.type}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          >
            <option value="CREDIT">Credit (Payment Received)</option>
            <option value="DEBIT">Debit (New Purchase)</option>
          </select>
        </div>

        {/* Amount */}
        <div>
          <label className="block mb-1 font-medium">
            Amount *
          </label>

          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />

          {errors.amount && (
            <p className="text-red-500 text-sm mt-1">
              {errors.amount}
            </p>
          )}
        </div>

        {/* Description */}
        <div>
          <label className="block mb-1 font-medium">
            Note *
          </label>

          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />

          {errors.description && (
            <p className="text-red-500 text-sm mt-1">
              {errors.description}
            </p>
          )}
        </div>

        {/* Backend error */}
        {serverError && (
          <div className="bg-red-100 text-red-700 p-3 rounded-lg">
            {serverError}
          </div>
        )}

        {/* Buttons */}
        <div className="flex gap-3 pt-3">

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg"
          >
            Save Entry
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

export default AddEntryPage;