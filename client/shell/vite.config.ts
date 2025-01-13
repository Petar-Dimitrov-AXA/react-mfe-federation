import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import federation from '@originjs/vite-plugin-federation'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  base: '',
  plugins: [
    react(),
    federation({
      name: "shell",
      remotes: {
        app1: "http://app1-am-hub.apps-crc.testing/assets/app1.js",
        app2: "http://app2-am-hub.apps-crc.testing/assets/app2.js",
        app3: "http://app3-am-hub.apps-crc.testing/assets/app3.js",
      },
      shared: ["react", "react-dom","react-router-dom"],
    })
  ],
  server: {
    port: 3000
  },
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
  resolve: {
    alias: {
      '@react-mfe-federation/shared-ui': resolve(__dirname, '../../libs/shared-ui/src'),
    }
  }
})