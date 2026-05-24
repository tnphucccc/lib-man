import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../store/store";
import { removeToast, Toast } from "../store/toastSlice";

const colorByLevel: Record<Toast["level"], string> = {
    error: "bg-red-500",
    success: "bg-green-500",
    info: "bg-blue-500",
};

function ToastItem({ toast }: { toast: Toast }) {
    const dispatch = useDispatch();
    useEffect(() => {
        const timer = setTimeout(() => dispatch(removeToast(toast.id)), 4000);
        return () => clearTimeout(timer);
    }, [toast.id, dispatch]);

    return (
        <div
            className={`${colorByLevel[toast.level]} text-white px-4 py-2 rounded-lg shadow-lg max-w-sm whitespace-pre-line cursor-pointer`}
            onClick={() => dispatch(removeToast(toast.id))}
        >
            {toast.message}
        </div>
    );
}

export default function ToastViewport() {
    const toasts = useSelector((state: RootState) => state.toasts.toasts);
    return (
        <div className="fixed top-4 right-4 z-50 flex flex-col gap-2">
            {toasts.map((t) => (
                <ToastItem key={t.id} toast={t} />
            ))}
        </div>
    );
}
