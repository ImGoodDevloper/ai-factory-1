<script setup lang="ts">
import { ref, onMounted } from 'vue';
import client from '@/api/client';
import type { AuditLogDto } from '@/types';

const auditLogs = ref<AuditLogDto[]>([]);
const isLoading = ref(true);

async function fetchAuditLogs() {
  isLoading.value = true;
  try {
    const response = await client.get<AuditLogDto[]>('/audit-logs');
    auditLogs.value = response.data;
  } catch (error) {
    console.error('Failed to fetch audit logs:', error);
  } finally {
    isLoading.value = false;
  }
}

onMounted(() => {
  fetchAuditLogs();
});

function formatDate(dateString: string) {
  return new Date(dateString).toLocaleString();
}
</script>

<template>
  <div class="audit-log-container">
    <h1>Audit Log</h1>
    
    <div v-if="isLoading" class="loading">Loading audit logs...</div>
    
    <div v-else-if="auditLogs.length === 0" class="no-data">
      No audit logs found.
    </div>
    
    <table v-else class="audit-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Timestamp</th>
          <th>User</th>
          <th>Action</th>
          <th>Resource</th>
          <th>Details</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="log in auditLogs" :key="log.id">
          <td>{{ log.id }}</td>
          <td>{{ formatDate(log.timestamp) }}</td>
          <td>{{ log.username }}</td>
          <td><span :class="['action-badge', log.action.toLowerCase()]">{{ log.action }}</span></td>
          <td>{{ log.resource }}</td>
          <td>{{ log.details }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.audit-log-container {
  padding: 2rem;
}

h1 {
  margin-bottom: 2rem;
  color: #2c3e50;
}

.loading, .no-data {
  text-align: center;
  padding: 2rem;
  font-style: italic;
  color: #666;
}

.audit-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.audit-table th, .audit-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.audit-table th {
  background-color: #f8f9fa;
  font-weight: bold;
  color: #2c3e50;
}

.audit-table tr:hover {
  background-color: #fcfcfc;
}

.action-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: bold;
  text-transform: uppercase;
}

.action-badge.create { background-color: #e3f2fd; color: #1976d2; }
.action-badge.update { background-color: #fff3e0; color: #f57c00; }
.action-badge.delete { background-color: #ffebee; color: #d32f2f; }
.action-badge.login { background-color: #e8f5e9; color: #388e3c; }
</style>
