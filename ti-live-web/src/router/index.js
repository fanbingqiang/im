import { createRouter, createWebHistory } from 'vue-router';
import Layout from './views/Layout.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('./views/Login.vue'),
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('./views/Dashboard.vue'),
      },
      {
        path: 'live',
        name: 'Live',
        component: () => import('./views/Live.vue'),
      },
      {
        path: 'broadcast',
        name: 'Broadcast',
        component: () => import('./views/Broadcast.vue'),
      },
      {
        path: 'chatroom',
        name: 'ChatRoom',
        component: () => import('./views/ChatRoom.vue'),
      },
      {
        path: 'live-room/:roomId',
        name: 'LiveRoom',
        component: () => import('./views/LiveRoom.vue'),
        props: true,
      },
      {
        path: 'vod',
        name: 'Vod',
        component: () => import('./views/VodPage.vue'),
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('./views/About.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
