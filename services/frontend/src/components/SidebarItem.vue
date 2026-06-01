<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { usePageStore } from '@/stores/pageStore';
import { useAuthStore } from '@/stores/authStore';
import type { PageSummaryDto } from '@/types';

const props = defineProps<{
  page: PageSummaryDto;
  depth: number;
}>();

const router = useRouter();
const pageStore = usePageStore();
const authStore = useAuthStore();

const isOpen = ref(false);
const children = computed(() => pageStore.childrenMap[props.page.id] || []);

const toggle = async () => {
  if (props.page.hasChildren) {
    if (!isOpen.value && children.value.length === 0) {
      await pageStore.fetchPageDetail(props.page.id);
    }
    isOpen.value = !isOpen.value;
  }
  navigateToPage();
};

const navigateToPage = () => {
  router.push(`/pages/${props.page.id}`);
};

const onCreateChild = async (e: Event) => {
  e.stopPropagation();
  const title = prompt('Enter page title:');
  if (title) {
    const newPage = await pageStore.createPage({ title, parentId: props.page.id });
    if (newPage) {
      isOpen.value = true;
      router.push(`/pages/${newPage.id}`);
    }
  }
};

const onRename = (e: Event) => {
  e.stopPropagation();
  const title = prompt('Enter new title:', props.page.title);
  if (title && title !== props.page.title) {
    // We need the full detail to update, but here we just want to rename.
    // The store's updatePage requires PageUpdateDto which includes content.
    // This might be a bit tricky if we don't have the content yet.
    // For now, let's assume we fetch it first or the API supports partial updates.
    // Actually, the store's updatePage takes PageUpdateDto.
    // Let's fetch detail first to get current content.
    pageStore.fetchPageDetail(props.page.id).then(() => {
      if (pageStore.currentPage) {
        pageStore.updatePage(props.page.id, {
          title,
          content: pageStore.currentPage.content,
          isLocked: pageStore.currentPage.isLocked
        });
      }
    });
  }
};

const onDelete = (e: Event) => {
  e.stopPropagation();
  if (confirm(`Are you sure you want to delete "${props.page.title}"?`)) {
    pageStore.deletePage(props.page.id);
  }
};
</script>

<template>
  <div class="sidebar-item-container">
    <div 
      class="sidebar-item flex items-center justify-between p-2 cursor-pointer hover-bg"
      :style="{ paddingLeft: depth * 12 + 8 + 'px' }"
      @click="toggle"
    >
      <div class="flex items-center gap-2 overflow-hidden">
        <span v-if="page.hasChildren" class="toggle-icon" :class="{ 'is-open': isOpen }">
          ▶
        </span>
        <span v-else class="spacer"></span>
        <span class="title truncate text-sm">{{ page.title }}</span>
      </div>
      
      <div v-if="authStore.isEditor" class="actions flex items-center gap-1">
        <button class="icon-btn" title="Add Child" @click="onCreateChild">＋</button>
        <button class="icon-btn" title="Rename" @click="onRename">✎</button>
        <button class="icon-btn danger" title="Delete" @click="onDelete">×</button>
      </div>
    </div>

    <div v-if="isOpen && children.length > 0" class="children">
      <SidebarItem 
        v-for="child in children" 
        :key="child.id" 
        :page="child" 
        :depth="depth + 1" 
      />
    </div>
  </div>
</template>

<style scoped>
.sidebar-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.toggle-icon {
  font-size: 10px;
  transition: transform var(--transition-speed);
  display: inline-block;
  width: 12px;
}

.toggle-icon.is-open {
  transform: rotate(90deg);
}

.spacer {
  width: 12px;
}

.title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.actions {
  opacity: 0;
  transition: opacity var(--transition-speed);
}

.sidebar-item:hover .actions {
  opacity: 1;
}

.icon-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 2px 4px;
  font-size: 14px;
  color: var(--text-muted);
  border-radius: 4px;
}

.icon-btn:hover {
  background-color: rgba(0, 0, 0, 0.1);
  color: var(--text-color);
}

.icon-btn.danger:hover {
  color: var(--danger-color);
}

.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
