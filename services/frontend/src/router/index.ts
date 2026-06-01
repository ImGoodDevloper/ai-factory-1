import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
  },
  {
    path: '/pages/:id',
    name: 'PageDetail',
    component: () => import('@/views/PageView.vue'),
    props: true,
  },
  {
    path: '/audit-log',
    name: 'AuditLog',
    component: () => import('@/views/AuditLogView.vue'),
    meta: { requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  
  if (to.meta.public) {
    if (authStore.isAuthenticated && to.name === 'Login') {
      next({ name: 'Home' });
    } else {
      next();
    }
    return;
  }

  if (!authStore.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } });
    return;
  }

  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    next({ name: 'Home' });
    return;
  }

  next();
});

export default router;
