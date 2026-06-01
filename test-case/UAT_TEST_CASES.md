# User Acceptance Test (UAT) Cases - Knowledge Encyclopedia System (KES)

This document outlines the functional test cases for the KES, designed to simulate exploratory, non-technical behavior by a business user.

## 1. Authentication & Access Control

### TC-1.1: Successful Login
- **Action**: Navigate to `/login`, enter valid credentials (e.g., `admin`/`admin123`), and click "Login".
- **Expected Result**: User is redirected to the Home view, a success notification appears, and the sidebar displays the page tree.

### TC-1.2: Unauthorized Access Attempt
- **Action**: Log out, then try to navigate directly to `/audit-log`.
- **Expected Result**: User is redirected to the login page.

### TC-1.3: Role-Based UI Visibility (RBAC)
- **Action**: Log in as a user with `ROLE_USER` (e.g., `user`/`user123`).
- **Expected Result**: The "Audit Log" link should NOT be visible in the navigation. Attempting to access `/audit-log` manually should result in a 403 error or redirection.

## 2. Page Management (CRUD)

### TC-2.1: Create a New Root Page
- **Action**: Click the "New Page" button in the sidebar. Enter "Project Alpha" in the modal and confirm.
- **Expected Result**: "Project Alpha" appears in the sidebar. The view switches to the new page.

### TC-2.2: Create a Nested Sub-page
- **Action**: Click the "+" icon next to "Project Alpha" in the sidebar. Enter "Specifications" and confirm.
- **Expected Result**: "Specifications" appears nested under "Project Alpha".

### TC-2.3: Edit and Save Content
- **Action**: Select "Specifications". Type `# Spec v1` in the editor.
- **Expected Result**: The preview pane updates instantly. The "isDirty" flag (if visible) indicates unsaved changes until auto-save triggers.

### TC-2.4: Rename a Page
- **Action**: Click the "Rename" (pencil) icon next to "Project Alpha". Change it to "Project Omega".
- **Expected Result**: The sidebar updates to show "Project Omega".

### TC-2.5: Delete a Page
- **Action**: Click the "Delete" (trash) icon next to "Project Omega". Confirm the deletion in the modal.
- **Expected Result**: "Project Omega" and its sub-page "Specifications" are removed from the sidebar.

## 3. Markdown Editor Features

### TC-3.1: Live Preview
- **Action**: Type `**Bold Text**` and `*Italic Text*` in the editor.
- **Expected Result**: The preview pane shows **Bold Text** and *Italic Text* correctly formatted.

### TC-3.2: Synchronized Scrolling
- **Action**: Create a long page with many lines of text. Scroll the editor.
- **Expected Result**: The preview pane scrolls automatically to match the editor's position.

## 4. Search & Media

### TC-4.1: Global Search
- **Action**: Type a keyword (e.g., "Alpha") into the search bar.
- **Expected Result**: A list of pages containing "Alpha" appears in the search results. Clicking a result navigates to that page.

### TC-4.2: Media Upload
- **Action**: In the editor, use the "Upload Image" feature or drag-and-drop an image.
- **Expected Result**: The image is uploaded to the server, and a Markdown link (e.g., `![image](/api/media/...)`) is inserted into the editor. The image renders in the preview.

## 5. Audit & System Health

### TC-5.1: View Audit Logs (Admin Only)
- **Action**: Log in as `admin`. Navigate to the "Audit Log" view.
- **Expected Result**: A table shows recent actions (LOGIN, PAGE_CREATE, PAGE_UPDATE) with timestamps, user IDs, and details.

### TC-5.2: Error Handling (RFC 7807)
- **Action**: Trigger a server error (e.g., try to create a page with an invalid parent ID via API if possible, or simulate a network failure).
- **Expected Result**: The frontend displays a clear error message parsed from the RFC 7807 response (title, detail).
