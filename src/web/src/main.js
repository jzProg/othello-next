import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'

const app = createApp(App)
            .use(VueAxios, axios)
            .mount('#app');

app.config = {
  devtools: true
}
