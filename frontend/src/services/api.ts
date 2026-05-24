import axios from "axios";
import { store } from "../store/store";
import { addToast } from "../store/toastSlice";

const api = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL ?? "http://localhost:8080/api/v1",
    withCredentials: true,
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response?.status;
        const method = error.config?.method?.toUpperCase();
        const url = error.config?.url;
        const data = error.response?.data;
        const message = data?.message ?? error.message;

        console.error(`[API] ${status ?? "NETWORK"} ${method ?? ""} ${url ?? ""} — ${message}`);

        const fieldErrors = data?.fieldErrors;
        let toastMessage: string;
        if (Array.isArray(fieldErrors) && fieldErrors.length > 0) {
            toastMessage = fieldErrors
                .map((fe: { field?: string; message?: string }) => `${fe.field ?? "field"}: ${fe.message ?? "invalid"}`)
                .join("\n");
        } else if (!error.response) {
            toastMessage = "Network error — is the server reachable?";
        } else {
            toastMessage = message ?? "Something went wrong";
        }

        store.dispatch(addToast({ message: toastMessage, level: "error" }));

        return Promise.reject(error);
    }
);

export default api;
