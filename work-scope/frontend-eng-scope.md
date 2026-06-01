# Work Scope: Frontend Engineer (frontend-eng)

## Context Optimization & Scope (STRICT)
1. **Logic & Integration Focus:** Your primary domain is `services/frontend/src/views/`, `services/frontend/src/stores/`, `services/frontend/src/router/`, and `services/frontend/src/api/`.
2. **Surgical Reading:** Use `start_line` and `end_line` for files > 50 lines.
3. **Collaborative Boundaries:**
   - **Infrastructure:** Coordinate with `frontend-infra` for API client setup.
   - **UI/UX:** Coordinate with `ui-ux-eng` for component styling.
   - **Editor:** Coordinate with `editor-eng` for Markdown editor integration.

## Core Mandates
### 1. Data Orchestration
- **Recursive Tree:** Logic for infinite nesting of pages.
- **Pinia Stores:** Feature-specific state management.
- **API Integration:** Axios client usage and error handling.

### 2. Feature Implementation
- **Navigation Guards:** Authentication and unsaved changes checks.
- **Contextual CRUD:** Tree-based page management.

### 3. Safety & Reliability
- **Type Safety:** Strict TypeScript and type-only imports.
- **Safety Guards:** User confirmation for destructive actions.
- **Media Handling:** Nginx media URL integration.

## Definition of Done (DoD)
- Logic Validation: Matches requirements.
- Type Safety: Passes `vue-tsc`.
- Testing: Store/Util coverage > 90%.
- Integration: Successful API communication.
