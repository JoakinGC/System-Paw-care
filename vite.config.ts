import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import tsconfigPaths from 'vite-tsconfig-paths';
import { resolve } from 'node:path';
import { NodeGlobalsPolyfillPlugin } from '@esbuild-plugins/node-globals-polyfill';
import { NodeModulesPolyfillPlugin } from '@esbuild-plugins/node-modules-polyfill';
import nodePolyfills from 'rollup-plugin-node-polyfills';
import type { Plugin } from 'rollup';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());
  const SERVER_API_URL = env.VITE_SERVER_API_URL ?? 'http://localhost:8081';

  return {
    root: resolve(__dirname, 'src/main/webapp'),

    plugins: [react(), tsconfigPaths()],

    define: {
      __SERVER_API_URL__: JSON.stringify(SERVER_API_URL),
      process: { env: {} },          // tiny shim
    },

    resolve: {
      alias: [
        { find: 'path',           replacement: 'path-browserify' },
        { find: 'url',            replacement: 'url' },
        { find: 'fs',             replacement: resolve(__dirname, 'src/main/webapp/empty.ts') },
        { find: 'source-map-js',  replacement: resolve(__dirname, 'src/main/webapp/empty.ts') },
      ],
    },

    optimizeDeps: {
      esbuildOptions: {
        plugins: [
          NodeGlobalsPolyfillPlugin({ buffer: false, process: true }),
          NodeModulesPolyfillPlugin(),
        ],
      },
    },

    build: {
      outDir: resolve(__dirname, 'target/classes/static'),
      emptyOutDir: true,
      sourcemap: mode === 'development',
      rollupOptions: {
        plugins: [nodePolyfills() as Plugin],
      },
    },

    server: {
      port: 9000,
      strictPort: true,
      proxy: {
        '/api': {
          target: SERVER_API_URL,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  };
});
