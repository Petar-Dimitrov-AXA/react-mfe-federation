const { execSync } = require("child_process");
const fs = require("fs");
const path = require("path");

const libsDir = path.join(__dirname, "./");

fs.readdirSync(libsDir).forEach((lib) => {
    const libPath = path.join(libsDir, lib);
    if (fs.statSync(libPath).isDirectory()) {
        console.log(`Building ${lib}...`);
        execSync("npm run build", { cwd: libPath, stdio: "inherit" });
    }
});
