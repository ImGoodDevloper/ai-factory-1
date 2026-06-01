import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { User, UserRole, LoginResponse } from '@/types';
import client from '@/api/client';

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'));
  const user = ref<User | null>(JSON.parse(localStorage.getItem('user') || 'null'));

  const isAuthenticated = computed(() => !!token.value);
  const isAdmin = computed(() => user.value?.role === 'ADMIN');
  const isEditor = computed(() => user.value?.role === 'ADMIN' || user.value?.role === 'EDITOR');

  function setSession(loginResponse: LoginResponse) {
    token.value = loginResponse.token;
    user.value = {
      username: loginResponse.username,
      role: (loginResponse.roles && loginResponse.roles.length > 0) ? loginResponse.roles[0] as UserRole : 'VIEWER',
    };
    localStorage.setItem('token', loginResponse.token);
    localStorage.setItem('user', JSON.stringify(user.value));
  }

  function clearSession() {
    token.value = null;
    user.value = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  async function login(credentials: { username: string; password: string }) {
    try {
      const response = await client.post<LoginResponse>('/auth/login', credentials);
      setSession(response.data);
      return response.data;
    } catch (error) {
      clearSession();
      throw error;
    }
  }

  function logout() {
    clearSession();
  }

  return {
    token,
    user,
    isAuthenticated,
    isAdmin,
    isEditor,
    login,
    logout,
  };
});
