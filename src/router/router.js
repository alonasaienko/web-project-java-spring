import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Default/Login.vue';
import Signup from '../components/Default/Signup.vue';
import MainHomePage from '../components/Home/MainHomePage.vue';

const routes = [
    { path: '/home', component: MainHomePage, meta: { requiresAuth: true } },
    { path: '/login', component: Login },
    { path: '/signup', component: Signup },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});
export default router;
