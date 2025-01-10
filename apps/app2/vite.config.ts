import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import federation from '@originjs/vite-plugin-federation'

// https://vitejs.dev/config/
export default defineConfig({
  base: '',
  plugins: [
    react(),
    federation({
      name: "app2",
      filename: "app2.js",
      exposes: {
        "./App": "./src/App",
      },
      shared: ["react", "react-dom"],
    })
  ],
  build: {
    modulePreload: false,
    target: "esnext",
    minify: false,
    cssCodeSplit: false,
    outDir: "dist",
    rollupOptions: {
      output: {
        assetFileNames: "assets/[name].[ext]",
        entryFileNames: "assets/[name].js",
        chunkFileNames: "assets/[name].js",
      }
    }
  },
  preview: {
    host: "localhost",
    port: 3002,
    strictPort: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
})