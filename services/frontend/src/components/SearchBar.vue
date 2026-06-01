<script setup lang="ts">
import { ref, watch } from 'vue';
import { usePageStore } from '@/stores/pageStore';
import { useRouter } from 'vue-router';

const pageStore = usePageStore();
const router = useRouter();
const searchQuery = ref('');
const isSearching = ref(false);

const onSearch = async () => {
  if (searchQuery.value.trim()) {
    isSearching.value = true;
    await pageStore.searchPages(searchQuery.value);
  } else {
    isSearching.value = false;
    pageStore.searchResults = [];
  }
};

// Debounce search
let timeout: ReturnType<typeof setTimeout>;
watch(searchQuery, (newVal) => {
  clearTimeout(timeout);
  timeout = setTimeout(() => {
    onSearch();
  }, 300);
});

const navigateToPage = (id: number) => {
  router.push(`/pages/${id}`);
  searchQuery.value = '';
  isSearching.value = false;
  pageStore.searchResults = [];
};
</script>

<template>
  <div class="search-container p-2">
    <input 
      v-model="searchQuery"
      type="text" 
      placeholder="Search pages..." 
      class="search-input w-full p-1 text-sm border rounded"
    />
    
    <div v-if="isSearching && searchQuery" class="search-results mt-2 border rounded bg-white shadow-lg absolute z-10 w-64">
      <div v-if="pageStore.isLoading" class="p-2 text-xs text-muted">Searching...</div>
      <div v-else-if="pageStore.searchResults.length === 0" class="p-2 text-xs text-muted">No results found.</div>
      <ul v-else class="max-h-60 overflow-y-auto">
        <li 
          v-for="result in pageStore.searchResults" 
          :key="result.id"
          @click="navigateToPage(result.id)"
          class="p-2 text-sm hover:bg-gray-100 cursor-pointer border-b last:border-b-0"
        >
          {{ result.title }}
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.search-container {
  position: relative;
}

.search-input {
  background-color: var(--bg-secondary);
  border-color: var(--border-color);
  color: var(--text-color);
}

.search-results {
  background-color: var(--bg-primary);
  border-color: var(--border-color);
}

.search-results li:hover {
  background-color: var(--bg-secondary);
}

.text-muted {
  color: var(--text-muted);
}
</style>
