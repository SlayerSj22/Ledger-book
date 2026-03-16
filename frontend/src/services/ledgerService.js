import api from "./api";

export const getLedgerEntries = async (accountId) => {

  const res = await api.get(`/ledger/account/${accountId}`);

  return res.data.data;

};

export const getLedgerEntry = async (entryId) => {

  const res = await api.get(`/ledger/entry/${entryId}`);

  return res.data.data;

};

export const addLedgerEntry = async (payload) => {

  const res = await api.post(`/ledger/add`, payload);

  return res.data.data;

};

export const updateLedgerEntry = async (entryId, payload) => {

  const res = await api.put(`/ledger/entry/${entryId}`, payload);

  return res.data.data;

};

export const deleteLedgerEntry = async (entryId) => {

  await api.delete(`/ledger/entry/${entryId}`);

};