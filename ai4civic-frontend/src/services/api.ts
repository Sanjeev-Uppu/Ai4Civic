import axios from "axios";

const API_BASE = import.meta.env.VITE_API_URL;

if (!API_BASE) {
  throw new Error("VITE_API_URL is not defined");
}

export const api = axios.create({
  baseURL: `${API_BASE}/api`,
});