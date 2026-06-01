<script setup lang="ts">
import { ref, computed, watch, onUnmounted, nextTick } from 'vue';
import MarkdownIt from 'markdown-it';
import { usePageStore } from '@/stores/pageStore';
import type { PageUpdateDto, MediaUploadResponse } from '@/types';
import client from '@/api/client';

const props = defineProps<{
  modelValue: string;
  title: string;
  isLocked: boolean;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'save'): void;
}>();

const pageStore = usePageStore();
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
});

// Add lazy loading to images
const defaultImageRender = md.renderer.rules.image || function (tokens, idx, options, _env, self) {
  return self.renderToken(tokens, idx, options);
};

md.renderer.rules.image = (tokens, idx, options, env, self) => {
  const token = tokens[idx];
  token.attrPush(['loading', 'lazy']);
  return defaultImageRender(tokens, idx, options, env, self);
};

const content = ref(props.modelValue);
const editorPane = ref<HTMLDivElement | null>(null);
const previewPane = ref<HTMLDivElement | null>(null);
const textareaRef = ref<HTMLTextAreaElement | null>(null);
const fileInputRef = ref<HTMLInputElement | null>(null);
const isSyncing = ref(false);
const isUploading = ref(false);

const renderedMarkdown = computed(() => {
  return md.render(content.value || '');
});

watch(() => props.modelValue, (newVal) => {
  if (newVal !== content.value) {
    content.value = newVal;
  }
});

const handleInput = () => {
  emit('update:modelValue', content.value);
  pageStore.setDirty(true);
  debouncedAutoSave();
};

const handleKeydown = (e: KeyboardEvent) => {
  if ((e.ctrlKey || e.metaKey) && e.key === 's') {
    e.preventDefault();
    saveContent();
  } else if ((e.ctrlKey || e.metaKey) && e.key === 'b') {
    e.preventDefault();
    insertText('**', '**');
  } else if ((e.ctrlKey || e.metaKey) && e.key === 'i') {
    e.preventDefault();
    insertText('_', '_');
  } else if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
    e.preventDefault();
    insertText('[', '](url)');
  }
};

const insertText = (before: string, after: string) => {
  const textarea = textareaRef.value;
  if (!textarea) return;

  const start = textarea.selectionStart;
  const end = textarea.selectionEnd;
  const text = textarea.value;
  const selectedText = text.substring(start, end);
  const replacement = before + selectedText + after;

  content.value = text.substring(0, start) + replacement + text.substring(end);
  
  // Update model value and dirty state
  emit('update:modelValue', content.value);
  pageStore.setDirty(true);

  // Restore focus and selection
  nextTick(() => {
    textarea.focus();
    textarea.setSelectionRange(start + before.length, end + before.length);
  });
};

const triggerImageUpload = () => {
  fileInputRef.value?.click();
};

const handleImageUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('file', file);

  isUploading.value = true;
  try {
    const response = await client.post<MediaUploadResponse>('/media/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    const { filename } = response.data;
    // The requirement says syntax should be ![alt](/media/filename)
    insertText(`![${file.name}](/media/${filename})`, '');
  } catch (error) {
    console.error('Image upload failed:', error);
  } finally {
    isUploading.value = false;
    // Reset file input
    target.value = '';
  }
};

let autoSaveTimeout: number | null = null;
const debouncedAutoSave = () => {
  if (autoSaveTimeout) {
    clearTimeout(autoSaveTimeout);
  }
  autoSaveTimeout = window.setTimeout(() => {
    if (pageStore.isDirty && pageStore.currentPage) {
      saveContent();
    }
  }, 3000); // 3 seconds debounce for auto-save
};

const saveContent = async () => {
  if (!pageStore.currentPage || !pageStore.isDirty) return;
  
  const updateData: PageUpdateDto = {
    title: props.title,
    content: content.value,
    isLocked: props.isLocked,
  };
  
  try {
    await pageStore.updatePage(pageStore.currentPage.id, updateData);
    pageStore.setDirty(false);
    emit('save');
  } catch (error) {
    console.error('Failed to save:', error);
  }
};

const syncScroll = (source: 'editor' | 'preview') => {
  if (isSyncing.value) return;
  
  const editor = textareaRef.value;
  const preview = previewPane.value;

  if (!editor || !preview) return;

  isSyncing.value = true;

  if (source === 'editor') {
    const percentage = editor.scrollTop / (editor.scrollHeight - editor.clientHeight);
    preview.scrollTop = percentage * (preview.scrollHeight - preview.clientHeight);
  } else {
    const percentage = preview.scrollTop / (preview.scrollHeight - preview.clientHeight);
    editor.scrollTop = percentage * (editor.scrollHeight - editor.clientHeight);
  }

  // Use requestAnimationFrame to reset isSyncing after the scroll event has likely finished
  requestAnimationFrame(() => {
    requestAnimationFrame(() => {
      isSyncing.value = false;
    });
  });
};

onUnmounted(() => {
  if (autoSaveTimeout) {
    clearTimeout(autoSaveTimeout);
  }
});
</script>

<template>
  <div class="markdown-editor-container">
    <div class="editor-toolbar">
      <div class="toolbar-actions">
        <button @click="insertText('**', '**')" :disabled="isLocked" title="Bold (Ctrl+B)"><b>B</b></button>
        <button @click="insertText('_', '_')" :disabled="isLocked" title="Italic (Ctrl+I)"><i>I</i></button>
        <button @click="insertText('[', '](url)')" :disabled="isLocked" title="Link (Ctrl+K)">🔗</button>
        <button @click="triggerImageUpload" :disabled="isUploading || isLocked" title="Upload Image">
          {{ isUploading ? '...' : '🖼️' }}
        </button>
        <input 
          type="file" 
          ref="fileInputRef" 
          style="display: none" 
          accept="image/*" 
          @change="handleImageUpload"
        />
      </div>
      <div class="status-area">
        <span class="status-indicator" :class="{ 'is-dirty': pageStore.isDirty }">
          {{ pageStore.isDirty ? '● Unsaved Changes' : '✓ Saved' }}
        </span>
        <button 
          class="save-button" 
          @click="saveContent" 
          :disabled="!pageStore.isDirty || isLocked"
          title="Save (Ctrl+S)"
        >
          Save
        </button>
      </div>
    </div>
    <div class="split-view">
      <div class="editor-pane" ref="editorPane">
        <textarea
          ref="textareaRef"
          v-model="content"
          @input="handleInput"
          @keydown="handleKeydown"
          @scroll="syncScroll('editor')"
          placeholder="Enter Markdown content..."
          :disabled="isLocked"
        ></textarea>
      </div>
      <div 
        class="preview-pane markdown-body" 
        ref="previewPane" 
        @scroll="syncScroll('preview')"
        v-html="renderedMarkdown"
      ></div>
    </div>
  </div>
</template>

<style scoped>
.markdown-editor-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  border-top: 1px solid var(--border-color);
  overflow: hidden;
  background-color: var(--bg-color);
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  background-color: var(--sidebar-bg);
  border-bottom: 1px solid var(--border-color);
}

.toolbar-actions {
  display: flex;
  gap: 8px;
}

.toolbar-actions button {
  padding: 4px 8px;
  border: 1px solid var(--border-color);
  background: var(--bg-color);
  border-radius: 4px;
  cursor: pointer;
  min-width: 32px;
  color: var(--text-color);
}

.toolbar-actions button:hover {
  background: var(--border-color);
}

.status-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-indicator {
  font-size: 0.85rem;
  color: var(--primary-color);
}

.status-indicator.is-dirty {
  color: #e67e22;
}

.save-button {
  padding: 4px 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color var(--transition-speed);
}

.save-button:hover:not(:disabled) {
  background-color: var(--primary-hover);
}

.save-button:disabled {
  background-color: var(--border-color);
  color: var(--text-muted);
  cursor: not-allowed;
}

.split-view {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.editor-pane, .preview-pane {
  flex: 1;
  height: 100%;
  overflow-y: auto;
}

.editor-pane {
  border-right: 1px solid var(--border-color);
}

textarea {
  width: 100%;
  height: 100%;
  padding: 16px;
  border: none;
  resize: none;
  font-family: 'Fira Code', 'Courier New', Courier, monospace;
  font-size: 14px;
  line-height: 1.6;
  outline: none;
  background-color: transparent;
  color: var(--text-color);
}

.preview-pane {
  padding: 16px;
  background-color: var(--bg-color);
}

/* Basic Markdown Styling */
.markdown-body :deep(h1) { border-bottom: 1px solid var(--border-color); padding-bottom: 0.3em; margin-top: 0; }
.markdown-body :deep(img) { max-width: 100%; height: auto; }
.markdown-body :deep(code) { background-color: rgba(0,0,0,0.05); border-radius: 3px; padding: 0.2em 0.4em; font-family: monospace; }
.markdown-body :deep(pre) { background-color: var(--sidebar-bg); border-radius: 3px; padding: 16px; overflow: auto; border: 1px solid var(--border-color); }
.markdown-body :deep(blockquote) { border-left: 4px solid var(--border-color); color: var(--text-muted); padding: 0 1em; margin: 0; }
</style>
