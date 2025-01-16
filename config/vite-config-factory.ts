import {UserConfig} from 'vite';
import type {ModuleFormat} from 'rollup';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';
import {resolve} from 'path';
import dts from "vite-plugin-dts";

interface AppConfig {
    name: string;
    port?: number;
    isHost?: boolean;
    remotes?: Record<string, string>;
    isLib?: boolean;
}

export function createViteConfig({
                                     name,
                                     port,
                                     isHost = false,
                                     isLib = false,
                                     remotes = {}
                                 }: AppConfig): UserConfig {

    const projectRoot = process.cwd();
    const isClientApp = projectRoot.includes('client');

    const sharedUiPath = isClientApp
        ? resolve(projectRoot, '../../libs/shared-ui/src')
        : resolve(projectRoot, '../shared-ui/src');

    const baseConfig: UserConfig = {
        base: '',
        plugins: [
            react(),
            federation({
                name,
                ...(isHost ? {
                    remotes,
                    shared: ["react", "react-dom", "react-router-dom"],
                } : {
                    filename: `${name}.js`,
                    exposes: {
                        "./App": "./src/App",
                    },
                    shared: ["react", "react-dom", "react-router-dom"],
                }),
            })
        ],
        build: {
            target: "esnext",
            minify: false,
            cssCodeSplit: false,
            outDir: "dist",
            emptyOutDir: true,
            sourcemap: false,
            write: true,
            rollupOptions: {
                preserveEntrySignatures: 'strict',
                input: {
                    main: resolve(projectRoot, 'index.html')
                },
                output: {
                    dir: 'dist',
                    format: 'es' as ModuleFormat,
                    entryFileNames: 'assets/[name].js',
                    chunkFileNames: 'assets/[name].[hash].js',
                    assetFileNames: 'assets/[name].[hash].[ext]'
                }
            }
        },
        resolve: {
            alias: [
                {
                    find: '@react-mfe-federation/shared-ui',
                    replacement: sharedUiPath
                }
            ]
        }
    };

    if (isLib) {
        return {
            ...baseConfig,
            plugins: [
                react(),
                dts({
                    insertTypesEntry: true,
                }),
            ],
            build: {
                emptyOutDir: true,
                lib: {
                    entry: resolve(projectRoot, 'src/index.ts'),
                    name,
                    formats: ['es'],
                    fileName: (format) => `${name}.${format}.js`,
                },
                outDir: resolve(projectRoot, 'dist'),
                rollupOptions: {
                    external: ['react', 'react-dom'],
                    output: {
                        globals: {
                            react: 'React',
                            'react-dom': 'ReactDOM',
                        },
                    },
                },
            },
        };
    }

    if (!isHost && port) {
        return {
            ...baseConfig,
            preview: {
                host: "localhost",
                port,
                strictPort: true,
                headers: {
                    "Access-Control-Allow-Origin": "*",
                },
            },
        };
    }

    if (isHost && port) {
        return {
            ...baseConfig,
            server: {
                port
            }
        };
    }

    return baseConfig;
}