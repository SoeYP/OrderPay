// import './assets/main.css'

// import { createApp } from 'vue'
// import App from './App.vue'

// createApp(App).mount('#app')


import { createApp } from 'vue'
import App from './App.vue'
import router from './router'  // 추가

const app = createApp(App)
app.use(router)                // 추가
app.mount('#app')