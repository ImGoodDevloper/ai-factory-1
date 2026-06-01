# Work Scope: Frontend Core & Infrastructure Architect (frontend-infra)

## Context Optimization & Scope (STRICT)
1. **Core Domain:** `services/frontend/src/api/`, `services/frontend/src/types/`, `services/frontend/src/utils/`, `services/frontend/src/stores/` (architectural patterns), and root config files.
2. **Exclusion:** Avoid deep dives into `services/frontend/src/views/` or `services/frontend/src/components/` unless required for global state or infrastructure debugging.

## Core Mandates
### 1. Type Safety ([C-08])
- Maintain `tsconfig.json` and ensure strict null checks.
- Define global types and interfaces for the entire project.

### 2. Infrastructure
- Responsible for the global API Client and environment variable validation.
- Optimize Vite configuration for development and production builds.

### 3. Quality Assurance
- Maintain the Vitest testing framework.
- Enforce > 90% coverage on all "Core" logic.

## Definition of Done (DoD)
- Core logic coverage > 90%.
- System passes `vue-tsc` without any type violations.
- API Client handles all RFC 7807 error patterns from the backend.
