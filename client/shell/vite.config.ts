import {createViteConfig} from "../../config/vite-config-factory";

export default createViteConfig({
  name: 'shell',
  port: 3000,
  isHost: true,
  remotes: {
    app1: "http://app1-am-hub.apps-crc.testing/assets/app1.js",
    app2: "http://app2-am-hub.apps-crc.testing/assets/app2.js",
    app3: "http://app3-am-hub.apps-crc.testing/assets/app3.js",
  }
});