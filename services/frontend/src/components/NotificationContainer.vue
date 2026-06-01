<script setup lang="ts">
import { useNotificationStore } from '@/stores/notificationStore';

const notificationStore = useNotificationStore();
</script>

<template>
  <div class="notification-container">
    <TransitionGroup name="list">
      <div
        v-for="notification in notificationStore.notifications"
        :key="notification.id"
        :class="['notification', notification.type]"
        @click="notificationStore.removeNotification(notification.id)"
      >
        {{ notification.message }}
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.notification-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notification {
  padding: 12px 20px;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  min-width: 200px;
  max-width: 400px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.info { background-color: #2196f3; }
.success { background-color: #4caf50; }
.warning { background-color: #ff9800; }
.error { background-color: #f44336; }

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
