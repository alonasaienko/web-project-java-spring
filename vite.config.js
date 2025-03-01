//vite.config.js

import { fileURLToPath, URL } from 'node:url';
import { defineConfig, loadEnv, searchForWorkspaceRoot } from 'vite';
import vue from '@vitejs/plugin-vue';
import fs from 'fs';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
    const env = loadEnv(mode, process.cwd(), '');
    const rest_proxy = `http://${env.REST_PROXY || 'localhost'}:8081`;
    console.log('rest_proxy', rest_proxy);

    return {
        base: '/',
        plugins: [vue()],
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url))
            }
        },
        server: {
            fs: {
                allow: [searchForWorkspaceRoot(process.cwd()), '../../../tools/nodejs/']
            },
            https: {
                key: fs.readFileSync(path.resolve(__dirname, 'example.com+5-key.pem')),
                cert: fs.readFileSync(path.resolve(__dirname, 'example.com+5.pem')),
            },
            //public: 'https://localhost:8443/',
            port: 5173,
            strictPort: true,
            proxy: {
                '^/(api|auth)': {
                    target: rest_proxy,
                    secure: false,
                    changeOrigin: true,
                    autoRewrite: true,
                    ws: true,
                    configure: (proxy, _options) => {
                        proxy.on("error", (err, _req, _res) => {
                            console.log("proxy error", err);
                        });
                        proxy.on("proxyReq", (proxyReq, req, _res) => {
                            console.log(
                                `[${req.method}]`,
                                req.url,
                                " => ",
                                `[${proxyReq.method}]`,
                                `${proxyReq.protocol}//${proxyReq.host}${proxyReq.path}`,
                            );
                        });
                        proxy.on("proxyRes", (proxyRes, req, _res) => {
                            console.log(
                                "< =",
                                proxyRes.statusCode,
                                req.url,
                            );
                        });
                    },
                }
            }
        },
        optimizeDeps: {
            include: [
                "primevue/config",
                "primevue/usetoast",
                "primevue/toastservice",
                "primevue/useconfirm",
                "primevue/confirmationservice",
                "primevue/panel",
                "primevue/treetable",
                "primevue/fileupload",
                "primevue/dropdown"
            ],
            force: true
        },
        build: {
            outDir: env.BUILD_DIR || './build'
        }
    };
});
