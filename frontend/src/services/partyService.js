import axios from "axios";

const BASE_URL = "http://localhost:8000";

export const searchParties = async (query) => {

  const res = await axios.get(
    `${BASE_URL}/party/search?query=${query}`
  );

  return res.data.data;

};

//this is working fine
export const getPartyById = async (id) => {

  const res = await axios.get(`${BASE_URL}/party/${id}`);

  return res.data.data;

};

export const getAllParties = async () => {
  const res = await axios.get(`${BASE_URL}/party/all`);
  return res.data.data;
};

export const deleteParty = async (id) => {
  await axios.delete(`${BASE_URL}/party/${id}`);
};