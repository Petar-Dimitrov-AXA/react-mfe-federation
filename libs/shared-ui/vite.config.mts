import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'path';

export default defineConfig({
    plugins: [react()],
    build: {
        emptyOutDir: true,
        lib: {
            entry: resolve(__dirname, 'src/index.ts'),
            name: 'SharedUI', 
            formats: ['es'], 
            fileName: (format) => `shared-ui.${format}.js`,
        },
        outDir: resolve(__dirname, './dist/shared-ui'), 
        rollupOptions: {
            external: ['react', 'react-dom'], // Mark external dependencies that should not be bundled
            output: {
                globals: {
                    react: 'React', // Global name for React in UMD build
                    'react-dom': 'ReactDOM', // Global name for ReactDOM in UMD build
                },
            },
        },
    },
});