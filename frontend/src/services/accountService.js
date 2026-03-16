import api from "./api";

export const getAccountsByParty = async (partyId) => {

  const res = await api.get(`/account/party/${partyId}`);

  return res.data.data;

};

export const createAccount = async (payload) => {

  const res = await api.post(`/account`, payload);

  return res.data.data;

};

export const deleteAccount = async (accountId) => {

  await api.delete(`/account/${accountId}`);

};

export const getAccountById = async (id) => {

  const res = await api.get(`/account/${id}`);

  return res.data.data;

};

export const getStatusById = async (id) => {

  const res = await api.get(`/account/${id}/status`);

  return res.data.data;

};