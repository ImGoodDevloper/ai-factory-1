# Frontend System Specification: Knowledge Encyclopedia (KES)

## 1.0 Global Definitions & Roadmap
- **Exposure**: $\text{Expose}(x) \iff \text{Output}(x) \in \text{PublicAPI}$.
- **Diagnostic Source**: $\text{Source}(E) = \text{Access}(\text{LogStorage}, \text{trace\_id}) \mid \text{Level} \geq \text{INTERNAL\_ADMIN}$.
- **Roadmap**: See [ROADMAP.md](./ROADMAP.md) for the implementation sequence $\Phi$.

## 1.1 System Definition ($\mathcal{S}$)
- **$\mathcal{F}$ (Frontend)**: $\{ (\text{Vue, 3.5.32}), (\text{Vite, 8.0.10}), (\text{TS, 6.0.2}), (\text{Pinia, 3.0.4}), (\text{Router, 5.0.6}) \}$

## 2.0 Data Models (Phase 2)
### 2.1 TypeScript Interfaces
```typescript
interface PageSummaryDto {
  id: number;
  title: string;
  hasChildren: boolean;
  parentId: number | null;
}

interface PageDetailDto {
  id: number;
  title: string;
  content: string;
  isLocked: boolean;
  parentId: number | null;
  children: PageSummaryDto[];
}

interface PageCreateDto {
  title: string;
  parentId?: number | null;
}

interface PageUpdateDto {
  title: string;
  content: string;
  isLocked: boolean;
}
```

## 1.2 Operational Protocols ($\mathcal{P}$)
- **[P-01] Auto-Activation (Frontend)**: 
    - $\forall t \in \text{Tasks}, \text{Type}(t) = \text{UI} \implies \text{Apply}(\text{Scroll-Lock} \wedge \text{Atomic CSS} \wedge \text{Nav Guards} \wedge \text{ContextualCRUD} \wedge \text{MarkdownLivePreview})$
    - **MarkdownLivePreview**: Implement an Obsidian-style editor where raw Markdown is editable and a synchronized preview is rendered in real-time.

## 1.3 Technical Constraints ($\mathcal{C}$)
1. **Consistency**: $\forall c \in \text{Colors}, c \in \text{CSS\_Variables}$.
2. **Editor Integrity**: $\text{Type}(t) = \text{Editor} \implies \text{Sync}(\text{ScrollPosition}) \wedge \text{Debounce}(\text{AutoSave})$.
8. **TypeSafety**: $\text{Type}(t) = \text{Frontend} \implies \text{Pass}(vue-tsc) \wedge \text{ExplicitType}(\text{ExternalLib}) \wedge \text{NoImplicitAny}(\text{Callbacks}) \wedge \text{TypeOnlyImport}(\text{VerbatimSyntax})$. All declarations must be consumed to prevent build-time failures.
10. **Accessibility**: $\forall a \in \text{UserActions}, \text{Visible}(a) \wedge \text{Iconic}(a)$. Non-technical accessibility is mandatory.
11. **SafetyGuards**: $\forall d \in \text{DestructiveActions}, \text{Trigger}(d) \implies \text{UserConfirmation} \wedge \text{ServerSideValidation}$.
12. **DependencySync**: $\text{Import}(Lib) \implies Lib \in \text{Manifest}(\text{package.json})$.
13. **ViewportIntegrity**: $\text{Type}(t) = \text{UI} \implies \text{Purge}(\text{BoilerplateCSS}) \wedge \text{Enable}(\text{FullViewport})$. Prevents layout drift from default framework styles.
14. **LayerPriority**: $\text{Component}(\text{Modal}) \implies \text{ZIndex}(\text{Top}) \wedge \text{Overlay}(\text{Lock})$. Ensures interactive elements are never obscured.
15. **ENV_Safety**: $\text{Import}(\text{ENV}) \implies \text{VerifiedBy}(\text{Schema})$. Ensure all environment variables match architecture-level definitions.
16. **Media Handling**: $\text{Render}(\text{Image}) \implies \text{Src}(\text{Nginx\_URL})$. Images must be loaded from the designated Nginx media endpoint.

## 3.1 Definition of Done ($\mathcal{D}o\mathcal{D}$)
A task $t$ is terminal if and only if $\text{Done}(t) = \text{True}$:
- $\text{Coverage}(\text{Vitest}) > 0.70$ (Total).
- **Core Precision**: $\text{Coverage}(\text{PiniaStores} \cup \text{Utils}) > 0.90$.
- $\text{Tested}(\text{Pinia}) \wedge \text{Mocked}(\text{API Failures})$

## 3.2 Self-Healing & Hygiene
- **Self-Healing ($\mathcal{H}$)**: $\text{Heal}(E) = \text{Analyze}(\text{Source}(E)) \to \text{Verify}(\text{Env}) \to \text{Refactor}(\text{Code}) \to \text{Update}(\text{Tests})$.
- **Privacy**: Public API responses must never contain StackTrace.
- **Hygiene**: $\mathcal{S}_{final} \implies \text{Update}(\text{PROGRESS.log})$. Language: Technical English / Traditional Chinese.
