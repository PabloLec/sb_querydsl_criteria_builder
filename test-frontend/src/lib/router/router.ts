import { createRouter, createWebHistory } from 'vue-router';

import App from '@/App.vue';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: App,
        props: (route) => ({ query: route.query.criteria })
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;