<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';

const props = defineProps<{
  show: boolean;
  title: string;
}>();

const emit = defineEmits(['close', 'confirm']);

const close = () => {
  emit('close');
};

const confirm = () => {
  emit('confirm');
};

// Scroll lock
onMounted(() => {
  if (props.show) {
    document.body.style.overflow = 'hidden';
  }
});

onUnmounted(() => {
  document.body.style.overflow = '';
});
</script>

<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="show" class="modal-overlay flex items-center justify-center" @click.self="close">
        <div class="modal-container bg-white p-4 rounded shadow-lg">
          <header class="modal-header border-b pb-2 mb-4 flex justify-between items-center">
            <h3 class="m-0">{{ title }}</h3>
            <button class="close-btn" @click="close">×</button>
          </header>
          <section class="modal-body mb-4">
            <slot></slot>
          </section>
          <footer class="modal-footer flex justify-end gap-2">
            <button class="btn btn-secondary" @click="close">Cancel</button>
            <button class="btn btn-primary" @click="confirm">Confirm</button>
          </footer>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.modal-container {
  min-width: 300px;
  max-width: 500px;
  background: var(--bg-color);
  border-radius: 8px;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid var(--border-color);
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: var(--primary-hover);
}

.btn-secondary {
  background-color: white;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style>
