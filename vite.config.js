import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";
import { resolve } from "node:path";

const resources = resolve(process.cwd(), "src/main/resources");

export default defineConfig({
    plugins: [tailwindcss()],
    root: resources,

    build: {
        outDir: resolve(resources, "static"),
        emptyOutDir: false,
        rollupOptions: {
            input: resolve(resources, "js/app.js"),
            output: {
                entryFileNames: "js/app.js",
                assetFileNames: (assetInfo) =>
                    assetInfo.name?.endsWith(".css")
                        ? "css/app.css"
                        : "assets/[name][extname]"
            }
        }
    }
});
