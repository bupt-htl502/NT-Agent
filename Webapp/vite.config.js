import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';

const config = loadEnv('development', './')
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': '/src',
    },
  },
  server: {
    proxy: {
      '/api': {
        target: config.VITE_API_URL,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
});