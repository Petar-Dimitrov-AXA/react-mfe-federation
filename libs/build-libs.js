// libs/build-libs.js
const { execSync } = require('child_process');
const path = require('path');
const fs = require('fs');

const libsDir = path.join(__dirname);
const libs = fs.readdirSync(libsDir)
    .filter(file => fs.statSync(path.join(libsDir, file)).isDirectory() && file !== 'build-config');

libs.forEach(lib => {
    console.log(`Building ${lib}...`);
    try {
        // First build TypeScript declarations
        execSync('tsc --project tsconfig.json --declaration --emitDeclarationOnly --outDir dist', {
            cwd: path.join(libsDir, lib),
            stdio: 'inherit'
        });

        // Then run the full build
        execSync('vite build', {
            cwd: path.join(libsDir, lib),
            stdio: 'inherit'
        });
    } catch (error) {
        console.error(`Failed to build ${lib}:`, error);
        process.exit(1);
    }
});