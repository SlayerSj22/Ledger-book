import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createAccount } from "../services/accountService";
import { toast } from "react-toastify";

function AddAccountPage() {

  const { id } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    totalAmount: "",
    description: ""
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");

  const validate = () => {

    const newErrors = {};

    if (!formData.totalAmount) {
      newErrors.totalAmount = "Amount required";
    }
    else if (formData.totalAmount <= 0) {
      newErrors.totalAmount = "Amount must be greater than 0";
    }

    if (!formData.description.trim()) {
      newErrors.description = "Description required";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {

    const { name, value } = e.target;

    setFormData({
      ...formData,
      [name]: value
    });

    setErrors({
      ...errors,
      [name]: ""
    });

    setServerError("");
  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    if (!validate()) return;

    try {

      const payload = {
        partyId: Number(id),
        totalAmount: Number(formData.totalAmount),
        description: formData.description
      };

      await createAccount(payload);

toast.success("Account created successfully");

navigate(`/party/${id}`, { state: { refresh: true } });

    } catch (err) {

      const message =
        err?.response?.data?.message ||
        "Failed to create account";

      toast.error(message);
    }

  };

  return (
    <div className="max-w-xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Create Account
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-xl shadow space-y-4"
      >

        {/* Amount */}
        <div>

          <input
            type="number"
            name="totalAmount"
            placeholder="Total Amount"
            value={formData.totalAmount}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />

          {errors.totalAmount && (
            <p className="text-red-500 text-sm mt-1">
              {errors.totalAmount}
            </p>
          )}

        </div>

        {/* Description */}
        <div>

          <textarea
            name="description"
            placeholder="Bill description"
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

        {/* Backend Error */}
        {serverError && (
          <div className="bg-red-100 text-red-700 p-3 rounded-lg">
            {serverError}
          </div>
        )}

        {/* Buttons */}
        <div className="flex gap-3">

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg"
          >
            Create
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

export default AddAccountPage;