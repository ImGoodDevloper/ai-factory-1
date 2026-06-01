# Work Scope: Editor & Interactive Feature Specialist (editor-eng)

## Context Optimization & Scope (STRICT)
1. **Editor Domain:** Focus exclusively on `services/frontend/src/components/MarkdownEditor.vue`、`services/frontend/src/utils/markdown/`, and their associated Pinia Stores.
2. **Surgical Reading:** Line-range reading must be used for complex parser files or large Vue components to preserve context window.
3. **Ignore Layout/Infra:** Do not read sidebar, navigation, or backend implementation details unless they directly impact editor functionality (e.g., auto-save APIs).

## Core Mandates
### 1. Editor Excellence ([P-01])
- **Obsidian-style Editor:** Deliver a high-fidelity Markdown editing experience featuring real-time preview.
- **Sync-Scroll:** Implement precise synchronized scrolling between the editor and preview panes.
- **Auto-save:** Implement debounced persistence logic.
- **Dirty Checking:** Accurately track unsaved change states to prevent data loss.

### 2. Technical Focus
- **Markdown Parsing:** Optimize rendering performance and support KES-specific syntax extensions.
- **Performance:** Ensure smooth operations through debouncing techniques, efficient DOM updates, and robust memory management for large documents.
- **Media Rendering:** Ensure media resources are correctly integrated with Nginx media endpoints.

### 3. UI/UX Integrity
- **Keyboard Shortcuts:** Implement productivity-enhancing keyboard shortcuts.
- **Visual Feedback:** Provide clear indicators for saving status and error alerts.

## Definition of Done (DoD)
- **Zero Lag:** No perceptible delay during typing or scrolling.
- **Parsing Fidelity:** 100% match between Markdown source code and rendered output within the supported syntax scope.
- **State Safety:** Auto-save and `isDirty` flags function flawlessly across all edge cases.
- **Test Coverage:** 
  - Markdown Parser/Utils: > 90%
  - Editor Component Logic: > 80%
- **Validation:** Code successfully passes `vue-tsc` checks and performance benchmarks.
