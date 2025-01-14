import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import federation from '@originjs/vite-plugin-federation'
import {resolve} from "path";

// https://vitejs.dev/config/
export default defineConfig({
  base: '',
  plugins: [
    react(),
    federation({
      name: "app1",
      filename: "app1.js",
      exposes: {
        "./App": "./src/App",
      },
      shared: ["react", "react-dom","react-router-dom"],
    }),
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
        main: resolve(__dirname, 'index.html')

      },
      output: {
        dir: 'dist',
        format: 'esm',
        entryFileNames: 'assets/[name].js',
        chunkFileNames: 'assets/[name].[hash].js',
        assetFileNames: 'assets/[name].[hash].[ext]'
      }
    }
  },
  preview: {
    host: "localhost",
    port: 3001,
    strictPort: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
})