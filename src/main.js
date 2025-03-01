//main.js

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import axios from 'axios';
import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import App from './App.vue';
import router from './router/router.js';
import { useUserStore } from './container/userstore';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import InputMask from 'primevue/inputmask';
import Menubar from 'primevue/menubar';
import InputNumber from 'primevue/inputnumber';

import Tabs from 'primevue/tabs';
import TabList from 'primevue/tablist';
import Tab from 'primevue/tab';
import TabPanels from 'primevue/tabpanels';
import TabPanel from 'primevue/tabpanel';

import Splitter from 'primevue/splitter';
import SplitterPanel from 'primevue/splitterpanel';

import DataTable from 'primevue/datatable';
import Column from 'primevue/column';


import ProgressBar from 'primevue/progressbar';

import Aura from '@primevue/themes/aura';
import { definePreset } from '@primevue/themes';


const pinia = createPinia();
const app = createApp(App);

app.use(pinia)
   .use(router)
   .use(ToastService);

const authStore = useUserStore();

axios.interceptors.request.use(function (config) {
    let token = authStore.authToken || localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

axios.interceptors.response.use(
    async (response) => {
        if (response.status === 201) {
            console.log('polling long wait process')
            let pollingResponse = await axios.get(response.headers.location);
            while (pollingResponse.status === 202) {
                await new Promise(resolve =>
                    setTimeout(async () => {
                        pollingResponse = await axios.get(response.headers.location)
                        resolve();
                    }, 2000));
            }
            return pollingResponse;
        }
        return response;
    },
    (error) => {
        console.log(error.request.responseURL);
        if (error.response && error.response.status === 401) {
            authStore.logout();
            if (!error.request.responseURL.match('/checktoken$')) {
                router.push({ name: 'login' });
            }
        }
        console.error(error.response ? error.response.data : error.message, error.request.responseURL);
        return Promise.reject(error);
    }
);


 const login = async (username, password) => {
    try {
        const response = await axios.post('/api/login', { username, password });
        authStore.setToken(response.data.token);
        router.push({ name: 'home' });
    } catch (error) {
        console.error('Login error:', error);
    }
};

const fetchUserProfile = async () => {
    try {
        const response = await axios.get('/api/user/profile');
        return response.data;
    } catch (error) {
        console.error('Error fetching user profile:', error);
        throw error;
    }
};

const logout = () => {
    authStore.logout();
    router.push({ name: 'login' });
};

export { login, fetchUserProfile, logout };

const MyPreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{sky.50}',
            100: '{sky.100}',
            200: '{sky.200}',
            300: '{sky.300}',
            400: '{sky.400}',
            500: '#9EB9DF',
            600: '#3B367C',
            700: '{sky.700}',
            800: '{sky.800}',
            900: '{sky.900}',
            950: '{sky.950}'
        }
    },
    colorScheme: {
        light: {
            formField: {
                hoverBorderColor: '{primary.color}'
            }
        },
        dark: {
            formField: {
                hoverBorderColor: '{primary.color}'
            }
        }
    },
    components: 
    {
        button: {
            paddingX: '0.3rem',
            paddingY: '0.3rem'
        },
        dialog: 
        {
            content:{
                padding: '1rem',
                background:'#9EB9DF'
            },
            background:'#e9f0f9',
            footer: {
                padding: '3rem'
            }
        }
    }
});

app.use(PrimeVue, {
    theme: {
        preset: MyPreset,
        options: {
            darkModeSelector: false || 'none',
        }
        //unstyled: true
    }
});

app.component('Button', Button);
app.component('Dialog', Dialog);
app.component('InputText', InputText);
app.component('Password', Password);
app.component('InputMask', InputMask);
app.component('Menubar', Menubar);
app.component('InputNumber', InputNumber);
app.component('Tabs', Tabs);
app.component('TabList', TabList);
app.component('Tab', Tab);
app.component('TabPanels', TabPanels);
app.component('TabPanel', TabPanel);
app.component('Splitter', Splitter);
app.component('SplitterPanel', SplitterPanel);
app.component('DataTable', DataTable);
app.component('Column', Column);
app.component('ProgressBar', ProgressBar);

app.mount('#app');
