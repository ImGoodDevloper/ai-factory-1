# Development Roadmap: Knowledge Encyclopedia (KES)

*This document defines the transformation phases $\Phi$ of the system state, extracted from the Global Common Specification.*

The development roadmap is a sequence $\Phi = \{ \phi_0, \phi_1, \phi_2, \phi_3, \phi_4, \phi_5, \phi_6, \phi_7 \}:

0. **$\phi_0$ (Allocation)**: $\text{Invoke}(\text{work-consultant}) \to \text{Define}(\text{Scopes}) \wedge \text{Update}(\text{Agents})$. **Status**: Completed.
1. **$\phi_1$ (Skeleton)**: $\text{Init}(\mathcal{B}, \mathcal{F}, \mathcal{I}) \mid \text{Tree}(\mathcal{S}) \approx \text{SKELETON.md}$. **Status**: Completed.
2. **$\phi_2$ (Model)**: $\{ \text{Page} \xrightarrow{f} \text{Parent} \mid \text{Page} \in \text{Entities} \}$. **Constraint**: Unlimited nesting depth. **Status**: Completed.
3. **$\phi_3$ (State)**: $\{ \text{PiniaStore}, \text{AxiosInterceptors}, \text{ErrorBoundaries} \}$. **Status**: Completed.
4. **$\phi_4$ (UI)**: $\text{Render}(\text{MarkdownLivePreview}) \wedge \text{Sync}(\text{isDirty}) \wedge \text{ScrollLock} \wedge \text{VisibleCRUD}$. **Status**: Completed.
5. **$\phi_5$ (Search/Media)**: $\text{RecursiveSearch}(\text{Repository}) \wedge \text{Upload}(\text{Media} \to \text{Nginx})$. **Status**: Completed.
6. **$\phi_6$ (Security/Auth)**: $\text{Auth}(\text{JWT}) \wedge \text{RBAC}(\text{User}) \wedge \text{AuditLog}(\text{Actions})$. **Status**: Completed.
7. **$\phi_7$ (Verification)**: $\text{Test}(\text{Cypress}) \wedge \text{Verify}(\text{E2E}) \wedge \text{Automate}(\text{UAT})$. **Status**: In Progress.

