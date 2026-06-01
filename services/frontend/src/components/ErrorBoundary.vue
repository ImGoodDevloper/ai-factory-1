<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue';

const error = ref<Error | null>(null);

onErrorCaptured((err) => {
  error.value = err;
  return false; // Prevent error from propagating further
});

const reset = () => {
  error.value = null;
};
</script>

<template>
  <div v-if="error" class="error-boundary">
    <div class="error-content">
      <h1>Something went wrong</h1>
      <p>{{ error.message }}</p>
      <button @click="reset">Try Again</button>
    </div>
  </div>
  <slot v-else></slot>
</template>

<style scoped>
.error-boundary {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  background-color: #f8d7da;
  color: #721c24;
  padding: 20px;
  text-align: center;
}

.error-content {
  max-width: 600px;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
  padding: 40px;
  background-color: white;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #721c24;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #491217;
}
</style>
