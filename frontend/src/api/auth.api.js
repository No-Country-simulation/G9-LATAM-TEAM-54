import apiClient from "./client";

export const loginApi = (email, password) =>
  apiClient.post("/auth/login", { email, password });

export const registerApi = (nombre, email, password) =>
  apiClient.post("/usuarios", { nombre, email, password });

export const getProfileApi = () =>
  apiClient.get("/usuarios/me");

export const setupInitialApi = (avgTemperatureC, householdSize) =>
  apiClient.put("/usuarios/configuracion-inicial", { avgTemperatureC, householdSize });
