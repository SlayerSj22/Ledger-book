import api from "./api";

export const searchParties = async (query) => {

  const res = await api.get(`/party/search?query=${query}`);

  return res.data.data;

};

export const getPartyById = async (id) => {

  const res = await api.get(`/party/${id}`);

  return res.data.data;

};

export const getAllParties = async () => {

  const res = await api.get(`/party`);

  return res.data.data;

};

export const deleteParty = async (id) => {

  await api.delete(`/party/${id}`);

};

export const createParty = async (payload) => {

  const res = await api.post(`/party`, payload);

  return res.data.data;

};