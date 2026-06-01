import { describe, it, expect, beforeEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { usePageStore } from '../pageStore';
import client from '@/api/client';

vi.mock('@/api/client', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  },
}));

describe('Page Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it('fetches root pages successfully', async () => {
    const store = usePageStore();
    const mockPages = [{ id: 1, title: 'Test Page', hasChildren: false, parentId: null }];
    (client.get as any).mockResolvedValueOnce({ data: mockPages });

    await store.fetchRootPages();

    expect(store.rootPages).toEqual(mockPages);
    expect(client.get).toHaveBeenCalledWith('/pages');
    expect(store.isLoading).toBe(false);
  });

  it('fetches page detail successfully', async () => {
    const store = usePageStore();
    const mockChildren = [{ id: 2, title: 'Child', hasChildren: false, parentId: 1 }];
    const mockPageDetail = { 
      id: 1, 
      title: 'Test Page', 
      content: 'Content', 
      isLocked: false, 
      parentId: null, 
      children: mockChildren 
    };
    (client.get as any).mockResolvedValueOnce({ data: mockPageDetail });

    await store.fetchPageDetail(1);

    expect(store.currentPage).toEqual(mockPageDetail);
    expect(store.childrenMap[1]).toEqual(mockChildren);
    expect(client.get).toHaveBeenCalledWith('/pages/1');
  });

  it('creates a root page successfully', async () => {
    const store = usePageStore();
    const newPage = { title: 'New Page', parentId: null };
    const createdPage = { id: 2, ...newPage, content: '', isLocked: false, children: [] };
    
    (client.post as any).mockResolvedValueOnce({ data: createdPage });
    (client.get as any).mockResolvedValueOnce({ data: [] }); // for fetchRootPages refresh

    const result = await store.createPage(newPage);

    expect(result).toEqual(createdPage);
    expect(client.post).toHaveBeenCalledWith('/pages', newPage);
    expect(client.get).toHaveBeenCalledWith('/pages');
  });

  it('updates a root page successfully', async () => {
    const store = usePageStore();
    const updateData = { title: 'Updated Title', content: 'Updated Content', isLocked: true };
    const updatedPage = { id: 1, ...updateData, parentId: null, children: [] };
    
    (client.put as any).mockResolvedValueOnce({ data: updatedPage });
    (client.get as any).mockResolvedValueOnce({ data: [] }); // for fetchRootPages refresh

    const result = await store.updatePage(1, updateData);

    expect(result).toEqual(updatedPage);
    expect(store.currentPage).toEqual(updatedPage);
    expect(client.put).toHaveBeenCalledWith('/pages/1', updateData);
    expect(client.get).toHaveBeenCalledWith('/pages');
  });

  it('deletes a root page successfully', async () => {
    const store = usePageStore();
    store.currentPage = { id: 1, title: 'T', content: 'C', isLocked: false, parentId: null, children: [] };
    
    (client.delete as any).mockResolvedValueOnce({});
    (client.get as any).mockResolvedValueOnce({ data: [] }); // for fetchRootPages refresh

    await store.deletePage(1);

    expect(store.currentPage).toBeNull();
    expect(client.delete).toHaveBeenCalledWith('/pages/1');
    expect(client.get).toHaveBeenCalledWith('/pages');
  });

  it('searches pages successfully', async () => {
    const store = usePageStore();
    const mockResults = [{ id: 1, title: 'Search Result', hasChildren: false, parentId: null }];
    (client.get as any).mockResolvedValueOnce({ data: mockResults });

    await store.searchPages('query');

    expect(store.searchResults).toEqual(mockResults);
    expect(client.get).toHaveBeenCalledWith('/search', { params: { q: 'query' } });
  });

  it('clears search results when query is empty', async () => {
    const store = usePageStore();
    store.searchResults = [{ id: 1, title: 'Result', hasChildren: false, parentId: null }];

    await store.searchPages('  ');

    expect(store.searchResults).toEqual([]);
    expect(client.get).not.toHaveBeenCalled();
  });
});
