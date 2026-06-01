<script setup lang="ts">
import { RouterView, useRoute, useRouter } from 'vue-router';
import { computed } from 'vue';
import ErrorBoundary from '@/components/ErrorBoundary.vue';
import NotificationContainer from '@/components/NotificationContainer.vue';
import SidebarTree from '@/components/SidebarTree.vue';
import { useAuthStore } from '@/stores/authStore';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const isPublicPage = computed(() => route.meta.public === true || !route.name);

function handleLogout() {
  authStore.logout();
  router.push({ name: 'Login' });
}
</script>

<template>
  <ErrorBoundary>
    <NotificationContainer />
    <div v-if="isPublicPage" class="h-full">
      <RouterView />
    </div>
    <div v-else class="app-layout flex h-full">
      <aside class="sidebar border-r flex flex-col">
        <div class="sidebar-header p-4 border-b flex items-center justify-between">
          <RouterLink to="/" class="font-bold text-decoration-none color-inherit">KES</RouterLink>
        </div>
        <div class="sidebar-content flex-1 overflow-auto">
          <SidebarTree />
        </div>
        <div v-if="authStore.user" class="sidebar-footer p-4 border-t">
          <div class="user-info mb-2">
            <div class="text-sm font-bold">{{ authStore.user.username }}</div>
            <div class="text-xs text-gray-500">{{ authStore.user.role }}</div>
          </div>
          <button @click="handleLogout" class="logout-btn text-xs">Logout</button>
        </div>
      </aside>
      <div class="main-container flex-1 flex flex-col overflow-hidden">
        <header class="main-header border-b p-2 flex items-center justify-between">
          <nav class="flex gap-4">
            <RouterLink to="/" class="text-sm">Home</RouterLink>
            <RouterLink v-if="authStore.isAdmin" to="/audit-log" class="text-sm">Audit Log</RouterLink>
          </nav>
        </header>
        <main class="flex-1 overflow-auto">
          <RouterView />
        </main>
      </div>
    </div>
  </ErrorBoundary>
</template>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  background-color: var(--sidebar-bg);
}

.main-header {
  height: var(--header-height);
}

.sidebar-header {
  height: var(--header-height);
}

.logout-btn {
  background: none;
  border: 1px solid #ddd;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #f5f5f5;
}
</style>
