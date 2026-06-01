export interface PageSummaryDto {
  id: number;
  title: string;
  hasChildren: boolean;
  parentId: number | null;
}

export interface PageDetailDto {
  id: number;
  title: string;
  content: string;
  isLocked: boolean;
  parentId: number | null;
  children: PageSummaryDto[];
}

export interface PageCreateDto {
  title: string;
  parentId?: number | null;
}

export interface PageUpdateDto {
  title: string;
  content: string;
  isLocked: boolean;
}

export interface MediaUploadResponse {
  url: string;
  filename: string;
}

export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  [key: string]: any;
}

export type UserRole = 'ADMIN' | 'EDITOR' | 'VIEWER';

export interface User {
  username: string;
  role: UserRole;
}

export interface LoginResponse {
  token: string;
  username: string;
  roles: string[];
}

export interface AuditLogDto {
  id: number;
  username: string;
  action: string;
  resource: string;
  timestamp: string;
  details: string;
}
