import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export type ToastLevel = "error" | "success" | "info";

export interface Toast {
    id: string;
    message: string;
    level: ToastLevel;
}

interface ToastState {
    toasts: Toast[];
}

const initialState: ToastState = { toasts: [] };

let counter = 0;
const nextId = () => `${Date.now()}-${counter++}`;

const toastSlice = createSlice({
    name: "toasts",
    initialState,
    reducers: {
        addToast: {
            reducer(state, action: PayloadAction<Toast>) {
                state.toasts.push(action.payload);
            },
            prepare(payload: { message: string; level?: ToastLevel }) {
                return {
                    payload: {
                        id: nextId(),
                        message: payload.message,
                        level: payload.level ?? "info",
                    },
                };
            },
        },
        removeToast(state, action: PayloadAction<string>) {
            state.toasts = state.toasts.filter((t) => t.id !== action.payload);
        },
    },
});

export const { addToast, removeToast } = toastSlice.actions;
export default toastSlice.reducer;
