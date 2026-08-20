import apiClient from "./client";

export const getDashboardActualApi = () =>
  apiClient.get("/dashboard/actual");
