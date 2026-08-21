import apiClient from "./client";

export const getHistorialReportesApi = () =>
  apiClient.get("/historial");

export const generarReporteApi = () =>
  apiClient.post("/analisis-energetico");

export const deleteReporteApi = (id) =>
  apiClient.delete(`/analisis/${id}`);
