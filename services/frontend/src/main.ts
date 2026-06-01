import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import pinia from './stores';
import { validateEnv } from './utils/env';

import './assets/main.css';

validateEnv();

const app = createApp(App);

app.use(pinia);
app.use(router);

app.mount('#app');
