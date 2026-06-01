import axios, { type AxiosInstance, type AxiosError } from 'axios';
import type { ProblemDetail } from '@/types';
import { useNotificationStore } from '@/stores/notificationStore';
import { useAuthStore } from '@/stores/authStore';
import pinia from '@/stores';

const baseURL = import.meta.env.VITE_API_BASE_URL || '/api';

const client: AxiosInstance = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor
client.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore(pinia);
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor
client.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ProblemDetail>) => {
    const notificationStore = useNotificationStore(pinia);
    const authStore = useAuthStore(pinia);
    
    if (error.response) {
      if (error.response.status === 401) {
        authStore.logout();
      }

      const problem = error.response.data;
      const message = problem?.detail || problem?.title || error.message || 'An unexpected error occurred';
      
      notificationStore.error(message);
      console.error('API Error:', problem);
    } else if (error.request) {
      notificationStore.error('Network error: No response received from server');
      console.error('Network Error:', error.request);
    } else {
      notificationStore.error(error.message);
      console.error('Error:', error.message);
    }
    
    return Promise.reject(error);
  }
);

export default client;
