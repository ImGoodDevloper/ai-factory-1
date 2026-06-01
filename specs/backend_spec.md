# Backend System Specification: Knowledge Encyclopedia (KES)

## 1.0 Global Definitions & Roadmap
- **Exposure**: $\text{Expose}(x) \iff \text{Output}(x) \in \text{PublicAPI}$.
- **Diagnostic Source**: $\text{Source}(E) = \text{Access}(\text{LogStorage}, \text{trace\_id}) \mid \text{Level} \geq \text{INTERNAL\_ADMIN}$.
- **Roadmap**: See [ROADMAP.md](./ROADMAP.md) for the implementation sequence $\Phi$.

## 1.1 System Definition ($\mathcal{S}$)
- **$\mathcal{B}$ (Backend)**: $\{ (\text{Spring Boot, 3.4.0}), (\text{JDK, 17}), (\text{Maven}), (\text{H2}) \}$

## 2.0 API Contract (Phase 2)

### 2.1 DTO Definitions
- **PageSummaryDto**: `{ id: Long, title: String, hasChildren: Boolean, parentId: Long }`
- **PageDetailDto**: `{ id: Long, title: String, content: String, isLocked: Boolean, parentId: Long, children: List<PageSummaryDto> }`
- **PageCreateDto**: `{ title: String, parentId: Long (Optional) }`
- **PageUpdateDto**: `{ title: String, content: String, isLocked: Boolean }`

### 2.2 REST Endpoints
| Method | Endpoint | Description | Input | Output |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/api/pages` | Get all root-level pages | None | `List<PageSummaryDto>` |
| GET | `/api/pages/{id}` | Get detailed page info | `id` | `PageDetailDto` |
| POST | `/api/pages` | Create a new page | `PageCreateDto` | `PageDetailDto` |
| PUT | `/api/pages/{id}` | Update page content/meta | `PageUpdateDto` | `PageDetailDto` |
| DELETE | `/api/pages/{id}` | Delete page (Recursive) | `id` | `204 No Content` |

### 2.3 Media & Search Endpoints (Phase 5)
| Method | Endpoint | Description | Input | Output |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/api/media/upload` | Upload image to Nginx storage | `MultipartFile` | `{ url: String, filename: String }` |
| GET | `/api/search` | Search pages by title/content | `q: String` | `List<PageSummaryDto>` |

## 1.2 Operational Protocols ($\mathcal{P}$)
- **[P-01] Auto-Activation (Backend)**: 
    - $\forall t \in \text{Tasks}, \text{Type}(t) = \text{Backend} \implies \text{Apply}(\text{DTO} \wedge \text{Layered Architecture} \wedge \text{Jakarta Validation})$
    - $\forall t \in \text{Tasks}, \text{Type}(t) = \text{DB} \implies \text{Apply}(\text{Unlimited Hierarchy} \wedge \text{Transactional Safety})$
    - **Unlimited Hierarchy**: `Page` entity must implement a self-referencing relationship (Adjacency List).
        - `Page` { `id`, `title`, `content`, `isLocked`, `parent` (ManyToOne), `children` (OneToMany, CascadeType.ALL, orphanRemoval=true) }
    - **Recursive Deletion**: Deleting a parent page MUST automatically delete all its descendants via JPA cascading or a recursive service-level operation.
    - **Content Storage**: Markdown content is stored as `CLOB` or `TEXT` within the database.

## 1.3 Technical Constraints ($\mathcal{C}$)
2. **Robustness**: $\forall s \in \text{ServiceMethods}, \text{Annotated}(s, @Transactional)$.
3. **Safety**: $\forall d \in \text{DTOs}, \text{Validated}(d, \text{RFC 7807})$.
    - **Privacy Constraint**: $\text{Response}(\text{RFC 7807}) \cap \text{StackTrace} = \emptyset$. Internal logs must capture full details with a unique `trace_id`.
4. **Separation (Strict Layering)**: $\text{Boundary}(\text{Controller}) \cap \text{Entities} = \emptyset$.
    - $\text{Map}(\text{DTO} \leftrightarrow \text{Entity}) \in \text{ServiceLayer} \setminus \text{ControllerLayer}$.
    - Service methods MUST return DTOs to the Controller.
5. **Media Integrity**: Backend provides URLs for media stored in $\mathcal{M}$ (Nginx), ensuring path consistency.
11. **SafetyGuards**: $\forall d \in \text{DestructiveActions}, \text{Trigger}(d) \implies \text{UserConfirmation} \wedge \text{ServerSideValidation}$.
12. **DependencySync**: $\text{Import}(Lib) \implies Lib \in \text{Manifest}(\text{pom.xml})$.
15. **StateGuard**: $\forall t \in \text{Tasks}, \text{Action}(t) \in \text{DestructiveActions} \implies \text{Apply}(\text{StatePreCheck}(\text{isLocked}))$. All deletions require server-side attribute verification.

## 3.1 Definition of Done ($\mathcal{D}o\mathcal{D}$)
A task $t$ is terminal if and only if $\text{Done}(t) = \text{True}$:
- $\text{Coverage}(\text{JUnit5}) > 0.80$
- $\text{Implemented}(\text{NullSafePartialUpdate})$
- $\text{Status}(\text{RFC 7807}) = \text{Active}$

## 3.2 Self-Healing & Hygiene
- **Self-Healing ($\mathcal{H}$)**: $\text{Heal}(E) = \text{Analyze}(\text{Source}(E)) \to \text{Verify}(\text{Env}) \to \text{Refactor}(\text{Code}) \to \text{Update}(\text{Tests})$.
- **Privacy**: Public API responses must never contain StackTrace.
- **Hygiene**: $\mathcal{S}_{final} \implies \text{Update}(\text{PROGRESS.log})$. Language: Technical English / Traditional Chinese.
