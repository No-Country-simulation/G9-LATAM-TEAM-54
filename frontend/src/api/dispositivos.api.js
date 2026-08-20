import apiClient from "./client";

export const getDispositivosApi = () =>
  apiClient.get("/dispositivos");

export const saveDispositivoApi = (data) =>
  apiClient.post("/dispositivos/guardar", data);

export const deleteDispositivoApi = (id) =>
  apiClient.delete(`/dispositivos/${id}`);
