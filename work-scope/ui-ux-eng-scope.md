# Work Scope: UI/UX & Design System Engineer (ui-ux-eng)

## Context Optimization & Scope (STRICT)
1. **UI/UX Focus:** Your primary domain is the frontend directory, specifically components and styling.
2. **Surgical Reading:** Use `start_line` and `end_line` for files > 50 lines.
3. **Logic Exclusion:** Minimize reading of Pinia stores or complex API service logic.

## Core Mandates
### 1. Visual Standards ([P-01])
- **Atomic CSS & Variables:** Use Atomic CSS and CSS Variables for all styling.
- **Viewport:** Maintain Full Viewport integrity; eliminate layout drift.
- **Iconography:** All actions must be "Iconic" and "Visible".

### 2. Interaction & Layering
- **Modals:** Implement `ZIndex(Top)` and `Overlay(Lock)` with Scroll-Lock.
- **Transitions:** Ensure smooth transitions between application states.

### 3. Accessibility & A11y
- **Keyboard Nav:** Ensure all interactive elements are keyboard accessible.
- **ARIA:** Use appropriate ARIA roles for complex components (Modals, Dropdowns).

### 4. Component Integrity
- **Prop Validation:** Strictly type all component props.
- **Slot Usage:** Use Vue slots for flexible, composable UI components.

## Boundary Definition
- **Primary Ownership:**
    - `services/frontend/src/components/` (UI Atoms/Molecules)
    - `services/frontend/src/assets/`
    - `services/frontend/src/style.css`
- **Shared Ownership (with frontend-eng):**
    - `services/frontend/src/views/` (Layout and visual structure)
- **Secondary/Read-Only:**
    - `services/frontend/src/stores/` (Read-only; limited to UI-related state flags)
    - `services/frontend/src/router/` (Read-only; limited to transition hooks)

## Definition of Done (DoD)
- **Visual Check:** Zero hardcoded styles; fully responsive design.
- **A11y Check:**Basic keyboard navigation and screen reader compatibility.
- **Build Check:** Passes `vue-tsc` and lint checks.
