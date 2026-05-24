import CounterReducer from "./reducers/CounterReducers";
import toastReducer from "./toastSlice";
import { thunk } from "redux-thunk";
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { configureStore, combineReducers } from "@reduxjs/toolkit";

const persistConfig = {
  key: 'root',
  storage,
  blacklist: ['toasts'],
}

const rootReducer = combineReducers({
  CounterStore: CounterReducer,
  toasts: toastReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => 
    getDefaultMiddleware({
      serializableCheck: {
        // Ignore redux-persist actions in serializability check
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'],
      },
    }).concat(thunk),
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

