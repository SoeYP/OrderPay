import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 3001,           // Vue 포트 (Node가 3000이면 3001 추천)
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // Node.js 백엔드 포트
        changeOrigin: true
      }
    }
  }
})
