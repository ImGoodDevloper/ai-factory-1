import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PageSummaryDto, PageDetailDto, PageCreateDto, PageUpdateDto } from '@/types';
import client from '@/api/client';

export const usePageStore = defineStore('page', () => {
  const rootPages = ref<PageSummaryDto[]>([]);
  const childrenMap = ref<Record<number, PageSummaryDto[]>>({});
  const currentPage = ref<PageDetailDto | null>(null);
  const searchResults = ref<PageSummaryDto[]>([]);
  const isLoading = ref(false);
  const isDirty = ref(false);

  const setDirty = (dirty: boolean) => {
    isDirty.value = dirty;
  };

  const fetchRootPages = async () => {
    isLoading.value = true;
    try {
      const response = await client.get<PageSummaryDto[]>('/pages');
      rootPages.value = response.data;
    } finally {
      isLoading.value = false;
    }
  };

  const searchPages = async (query: string) => {
    if (!query.trim()) {
      searchResults.value = [];
      return;
    }
    isLoading.value = true;
    try {
      const response = await client.get<PageSummaryDto[]>('/search', {
        params: { q: query }
      });
      searchResults.value = response.data;
    } finally {
      isLoading.value = false;
    }
  };

  const fetchPageDetail = async (id: number) => {
    isLoading.value = true;
    try {
      const response = await client.get<PageDetailDto>(`/pages/${id}`);
      currentPage.value = response.data;
      // Update children map with the children from the detail
      if (response.data.children) {
        childrenMap.value[id] = response.data.children;
      }
    } finally {
      isLoading.value = false;
    }
  };

  const createPage = async (page: PageCreateDto) => {
    isLoading.value = true;
    try {
      const response = await client.post<PageDetailDto>('/pages', page);
      if (page.parentId) {
        // Refresh parent's children
        await fetchPageDetail(page.parentId);
      } else {
        await fetchRootPages();
      }
      return response.data;
    } finally {
      isLoading.value = false;
    }
  };

  const updatePage = async (id: number, page: PageUpdateDto) => {
    isLoading.value = true;
    try {
      const response = await client.put<PageDetailDto>(`/pages/${id}`, page);
      currentPage.value = response.data;
      // If it's a root page, refresh root pages to update title
      if (response.data.parentId === null) {
        await fetchRootPages();
      } else {
        // Refresh parent to update title in children list
        await fetchPageDetail(response.data.parentId);
      }
      return response.data;
    } finally {
      isLoading.value = false;
    }
  };

  const deletePage = async (id: number) => {
    isLoading.value = true;
    try {
      const parentId = currentPage.value?.id === id ? currentPage.value.parentId : null;
      await client.delete(`/pages/${id}`);
      if (currentPage.value?.id === id) {
        currentPage.value = null;
      }
      if (parentId) {
        await fetchPageDetail(parentId);
      } else {
        await fetchRootPages();
      }
    } finally {
      isLoading.value = false;
    }
  };

  return {
    rootPages,
    childrenMap,
    currentPage,
    searchResults,
    isLoading,
    isDirty,
    setDirty,
    fetchRootPages,
    searchPages,
    fetchPageDetail,
    createPage,
    updatePage,
    deletePage,
  };
});
