import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";
import { randomUUID } from "node:crypto";
import { readdirSync } from "node:fs";
import { resolve } from "node:path";

const resources = resolve(process.cwd(), "src/main/resources");
const templates = resolve(resources, "templates");

export default defineConfig(({ mode }) => {
    const recargaLocal = mode === "recarga-local";

    return {
        plugins: [
            tailwindcss(),
            ...(recargaLocal ? [{
                name: "recarga-local",
                buildStart() {
                    for (const arquivo of readdirSync(templates, { recursive: true })) {
                        if (arquivo.endsWith(".html")) this.addWatchFile(resolve(templates, arquivo));
                    }
                },
                generateBundle() {
                    this.emitFile({
                        type: "asset",
                        fileName: "js/versao-frontend.txt",
                        source: randomUUID()
                    });
                }
            }] : [])
        ],
        root: resources,

        build: {
            outDir: recargaLocal ? resolve(resources, "../../../target/frontend-local") : resolve(resources, "static"),
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
    };
});
