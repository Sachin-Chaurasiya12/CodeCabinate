import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
    plugins: [react()],

    define: {
        global: "globalThis"
    },

    server: {
        host: "0.0.0.0",
        port: 3000,
        
        allowedHosts: [
            ".trycloudflare.com"
        ],

        hmr: {
            protocol: "ws",
            host: "localhost",
            clientPort: 80
        }
    }
});