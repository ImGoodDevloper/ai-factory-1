import { defineStore } from 'pinia';
import { ref } from 'vue';

export type NotificationType = 'info' | 'success' | 'warning' | 'error';

export interface Notification {
  id: string;
  type: NotificationType;
  message: string;
  timeout?: number;
}

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<Notification[]>([]);

  const addNotification = (notification: Omit<Notification, 'id'>) => {
    const id = Math.random().toString(36).substring(2, 9);
    const newNotification = { ...notification, id };
    notifications.value.push(newNotification);

    if (notification.timeout !== 0) {
      setTimeout(() => {
        removeNotification(id);
      }, notification.timeout || 5000);
    }
  };

  const removeNotification = (id: string) => {
    notifications.value = notifications.value.filter((n) => n.id !== id);
  };

  const error = (message: string, timeout?: number) => {
    addNotification({ type: 'error', message, timeout });
  };

  const success = (message: string, timeout?: number) => {
    addNotification({ type: 'success', message, timeout });
  };

  const info = (message: string, timeout?: number) => {
    addNotification({ type: 'info', message, timeout });
  };

  const warning = (message: string, timeout?: number) => {
    addNotification({ type: 'warning', message, timeout });
  };

  return {
    notifications,
    addNotification,
    removeNotification,
    error,
    success,
    info,
    warning,
  };
});
