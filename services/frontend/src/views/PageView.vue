<script setup lang="ts">
import { watch, onMounted } from 'vue';
import { usePageStore } from '@/stores/pageStore';
import { useAuthStore } from '@/stores/authStore';
import MarkdownEditor from '@/components/MarkdownEditor.vue';

const props = defineProps<{
  id: string;
}>();

const pageStore = usePageStore();
const authStore = useAuthStore();

const loadPage = () => {
  const pageId = parseInt(props.id);
  if (!isNaN(pageId)) {
    pageStore.fetchPageDetail(pageId);
  }
};

const onRename = () => {
  if (!pageStore.currentPage) return;
  const title = prompt('Enter new title:', pageStore.currentPage.title);
  if (title && title !== pageStore.currentPage.title) {
    pageStore.updatePage(pageStore.currentPage.id, {
      title,
      content: pageStore.currentPage.content,
      isLocked: pageStore.currentPage.isLocked
    });
  }
};

const onDelete = () => {
  if (!pageStore.currentPage) return;
  if (confirm(`Are you sure you want to delete "${pageStore.currentPage.title}"?`)) {
    pageStore.deletePage(pageStore.currentPage.id);
  }
};

onMounted(loadPage);
watch(() => props.id, loadPage);
</script>

<template>
  <div class="page-view h-full flex flex-col">
    <div v-if="pageStore.isLoading && !pageStore.currentPage" class="p-4">
      Loading page...
    </div>
    <div v-else-if="pageStore.currentPage" class="flex-1 flex flex-col overflow-hidden">
      <header class="page-header p-4 border-b flex justify-between items-center">
        <div class="flex items-center gap-2">
          <h2 class="m-0">{{ pageStore.currentPage.title }}</h2>
          <span v-if="pageStore.currentPage.isLocked" title="Locked">🔒</span>
        </div>
        <div v-if="authStore.isEditor" class="page-actions flex gap-2">
          <button class="icon-btn" title="Rename" @click="onRename">✎ Rename</button>
          <button class="icon-btn danger" title="Delete" @click="onDelete">× Delete</button>
        </div>
      </header>
      <div class="editor-container flex-1 overflow-hidden">
        <MarkdownEditor 
          v-model="pageStore.currentPage.content" 
          :title="pageStore.currentPage.title"
          :is-locked="pageStore.currentPage.isLocked || !authStore.isEditor"
          @update:modelValue="pageStore.setDirty(true)"
        />
      </div>
    </div>
    <div v-else class="p-4">
      Page not found.
    </div>
  </div>
</template>

<style scoped>
.page-view {
  height: 100%;
}

.editor-container {
  height: calc(100% - 60px); /* Adjust based on header height */
}
</style>
