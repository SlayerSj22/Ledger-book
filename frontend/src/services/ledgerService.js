import axios from "axios";

const BASE_URL = "http://localhost:8000";

export const getLedgerEntries = async (accountId) => {

  const res = await axios.get(
    `${BASE_URL}/ledger/account/${accountId}`
  );

  return res.data.data;
};

export const getLedgerEntry = async (entryId) => {

  const res = await axios.get(
    `${BASE_URL}/ledger/entry/${entryId}`
  );

  return res.data.data;
};

export const addLedgerEntry = async (payload) => {
  return axios.post(`${BASE_URL}/ledger/add`, payload);
};

export const updateLedgerEntry = async (entryId, payload) => {

  const res = await axios.put(
    `${BASE_URL}/ledger/entry/${entryId}`,
    payload
  );

  return res.data.data;
};

export const deleteLedgerEntry = async (entryId) => {

  const res = await axios.delete(
    `${BASE_URL}/ledger/entry/${entryId}`
  );

  return res.data.data;
};