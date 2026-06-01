<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePageStore } from '@/stores/pageStore';
import { useAuthStore } from '@/stores/authStore';
import SidebarItem from './SidebarItem.vue';
import SearchBar from './SearchBar.vue';

const pageStore = usePageStore();
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();

const shouldFetch = () => {
  return !!(authStore.isAuthenticated && route.name && !route.meta.public);
};

onMounted(() => {
  if (shouldFetch()) {
    pageStore.fetchRootPages();
  }
});

watch(() => authStore.isAuthenticated, (isAuth) => {
  if (isAuth && shouldFetch()) {
    pageStore.fetchRootPages();
  }
});

const onCreateRoot = async () => {
  const title = prompt('Enter root page title:');
  if (title) {
    const newPage = await pageStore.createPage({ title });
    if (newPage) {
      router.push(`/pages/${newPage.id}`);
    }
  }
};
</script>

<template>
  <div class="sidebar-tree">
    <SearchBar />
    <div class="tree-actions p-2 flex justify-between items-center">
      <span class="text-sm font-bold text-muted">PAGES</span>
      <button class="add-root-btn" @click="onCreateRoot" title="Add Root Page">＋</button>
    </div>
    <div v-if="pageStore.isLoading && pageStore.rootPages.length === 0" class="p-4 text-sm text-muted">
      Loading...
    </div>
    <div v-else-if="pageStore.rootPages.length === 0" class="p-4 text-sm text-muted">
      No pages found.
    </div>
    <div v-else class="tree-content">
      <SidebarItem 
        v-for="page in pageStore.rootPages" 
        :key="page.id" 
        :page="page" 
        :depth="0" 
      />
    </div>
  </div>
</template>

<style scoped>
.sidebar-tree {
  user-select: none;
}

.tree-actions {
  color: var(--text-muted);
}

.add-root-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  color: var(--text-muted);
  padding: 0 4px;
  border-radius: 4px;
}

.add-root-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
  color: var(--text-color);
}

.text-muted {
  color: var(--text-muted);
}
</style>
