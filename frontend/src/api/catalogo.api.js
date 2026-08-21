import apiClient from "./client";

export const getEstanciasApi = () =>
  apiClient.get("/estancias");

export const getEstanciaAnalisisApi = (id) =>
  apiClient.get(`/estancias/${id}/analisis`);

export const getCatalogoApi = () =>
  apiClient.get("/catalogo");

export const getVariantesApi = (equipoId) =>
  apiClient.get(`/catalogo/${equipoId}/variantes`);

export const getOpcionesTemperaturaApi = () =>
  apiClient.get("/configuracion/opciones-temperatura");
