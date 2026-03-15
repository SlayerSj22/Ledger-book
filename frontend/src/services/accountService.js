import axios from "axios";

const BASE_URL = "http://localhost:8000";

export const getAccountsByParty = async (partyId) => {

  const res = await axios.get(
    `${BASE_URL}/account/party/${partyId}`
  );

  return res.data.data;

};

export const createAccount = async (payload) => {

  const res = await axios.post(
    `${BASE_URL}/account/create`,
    payload
  );

  return res.data.data;
};

export const deleteAccount = async (accountId) => {

  await axios.delete(`${BASE_URL}/account/${accountId}`);

};

export const getAccountById = async (id) => {

  const res = await axios.get(
    `http://localhost:8000/account/${id}`
  );

  return res.data.data;

};

export const getStatusById = async(id) =>{
  const res = await axios.get(
    `http://localhost:8000/account/${id}/status`
  )
  return res.data.data;
}