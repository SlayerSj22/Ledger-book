import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AddPartyPage() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    fatherName: "",
    village: "",
    phone: ""
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");

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

  const validate = () => {

    const newErrors = {};

    if (!formData.name.trim()) newErrors.name = "Name required";
    if (!formData.fatherName.trim()) newErrors.fatherName = "Father name required";
    if (!formData.village.trim()) newErrors.village = "Village required";

    if (!formData.phone) {
      newErrors.phone = "Phone required";
    } 
    else if (!/^\d{10}$/.test(formData.phone)) {
      newErrors.phone = "Phone must be 10 digits";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    if (!validate()) return;

    try {

      const res = await axios.post(
        "http://localhost:8000/party/register",
        formData
      );

      const response = res.data;

      if (response.status === 201) {
        navigate("/");
      } 
      else {
        setServerError(response.message);
      }

    } catch {

      setServerError("Server error occurred");

    }

  };

  return (
    <div className="max-w-xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Register Customer
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-xl shadow space-y-4"
      >

        {/* Name */}
        <div>
          <input
            name="name"
            placeholder="Name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />
          {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
        </div>

        {/* Father Name */}
        <div>
          <input
            name="fatherName"
            placeholder="Father Name"
            value={formData.fatherName}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />
          {errors.fatherName && <p className="text-red-500 text-sm">{errors.fatherName}</p>}
        </div>

        {/* Village */}
        <div>
          <input
            name="village"
            placeholder="Village"
            value={formData.village}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />
          {errors.village && <p className="text-red-500 text-sm">{errors.village}</p>}
        </div>

        {/* Phone */}
        <div>
          <input
            name="phone"
            placeholder="Phone"
            value={formData.phone}
            onChange={handleChange}
            className="w-full border px-3 py-2 rounded-lg"
          />
          {errors.phone && <p className="text-red-500 text-sm">{errors.phone}</p>}
        </div>

        {/* Backend message */}
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
            Register
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

export default AddPartyPage;