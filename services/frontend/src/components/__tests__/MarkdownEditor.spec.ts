import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import MarkdownEditor from '../MarkdownEditor.vue';
import { createPinia, setActivePinia } from 'pinia';
import { usePageStore } from '@/stores/pageStore';

// Mock markdown-it
vi.mock('markdown-it', () => {
  return {
    default: vi.fn().mockImplementation(() => ({
      render: vi.fn((content) => `<div class="rendered">${content}</div>`),
      renderer: {
        rules: {
          image: vi.fn(),
        },
      },
    })),
  };
});

describe('MarkdownEditor.vue', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it('renders markdown content', async () => {
    const wrapper = mount(MarkdownEditor, {
      props: {
        modelValue: '# Hello',
        title: 'Test Page',
        isLocked: false,
      },
    });

    expect(wrapper.find('.preview-pane').html()).toContain('Hello');
  });

  it('updates content on textarea input', async () => {
    const wrapper = mount(MarkdownEditor, {
      props: {
        modelValue: '',
        title: 'Test Page',
        isLocked: false,
      },
    });

    const textarea = wrapper.find('textarea');
    await textarea.setValue('# New Content');

    expect(wrapper.emitted('update:modelValue')).toBeTruthy();
    expect(wrapper.emitted('update:modelValue')![0]).toEqual(['# New Content']);
    
    const pageStore = usePageStore();
    expect(pageStore.isDirty).toBe(true);
  });

  it('handles keyboard shortcuts', async () => {
    const wrapper = mount(MarkdownEditor, {
      props: {
        modelValue: 'text',
        title: 'Test Page',
        isLocked: false,
      },
    });

    const textarea = wrapper.find('textarea');
    // Select 'text'
    const el = textarea.element as HTMLTextAreaElement;
    el.setSelectionRange(0, 4);

    await textarea.trigger('keydown', { ctrlKey: true, key: 'b' });
    
    expect(wrapper.emitted('update:modelValue')![0]).toEqual(['**text**']);
  });

  it('disables textarea when isLocked is true', () => {
    const wrapper = mount(MarkdownEditor, {
      props: {
        modelValue: 'content',
        title: 'Test Page',
        isLocked: true,
      },
    });

    expect(wrapper.find('textarea').attributes('disabled')).toBeDefined();
  });
});
