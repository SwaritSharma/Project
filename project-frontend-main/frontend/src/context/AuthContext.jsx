import React, { createContext, useContext, useEffect, useState } from "react";

const AuthCtx = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [ready, setReady] = useState(false);

    useEffect(() => {
        const raw = localStorage.getItem("dg_user");
        if (raw) {
            try {
                setUser(JSON.parse(raw));
            } catch (_) {}
        }
        setReady(true);
    }, []);

    const login = (loginResp) => {
        localStorage.setItem("dg_token", loginResp.token);
        localStorage.setItem("dg_user", JSON.stringify(loginResp));
        setUser(loginResp);
    };

    const logout = () => {
        localStorage.removeItem("dg_token");
        localStorage.removeItem("dg_user");
        setUser(null);
    };

    return (
        <AuthCtx.Provider value={{ user, ready, login, logout }}>
            {children}
        </AuthCtx.Provider>
    );
}

export const useAuth = () => useContext(AuthCtx);
