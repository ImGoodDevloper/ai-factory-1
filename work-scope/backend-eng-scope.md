# Work Scope: Backend Engineer Specialist (backend-eng)

## Context Optimization & Scope (STRICT)
1. **Backend Focus:** Your primary domain is the `src/main/java/` and `src/main/resources/` directories.
2. **Surgical Reading:** For Java files or XML/YAML configs exceeding 50 lines, you MUST use `start_line` and `end_line` with `read_file`.
3. **Ignore Frontend:** Do not read `.vue`, `.ts` (frontend), or CSS files unless explicitly required for integration or API contract validation.

## Core Mandates
### 1. Architecture & Layering ([P-01])
- **Strict Layering:** `Controller` $\cap$ `Entities` = $\emptyset$. Controllers must ONLY interact with DTOs.
- **DTO Mapping:** Mapping between DTOs and Entities MUST occur in the Service Layer.
- **Validation:** Use Jakarta Validation for all DTOs. Implement `NullSafePartialUpdate` patterns.

### 2. Robustness & Safety
- **Transactions:** All Service methods MUST be annotated with `@Transactional`.
- **Error Handling:** Implement **RFC 7807** standard.
- **Privacy:** Public API responses MUST NOT contain StackTraces. Internal logs must capture details with a unique `trace_id`.

### 3. Database & State ([P-01], [C-15])
- **Recursive Hierarchy:** Implement self-referencing Page entity (Adjacency List) for infinite nesting.
- **Media URLs:** Provide URLs compatible with Nginx `/media/` routing.
- **StateGuard:** Destructive actions (deletions) MUST apply `StatePreCheck(isLocked)` via server-side verification.

### 4. Dependency Sync
- Ensure all imported libraries are declared in `pom.xml`.

## Definition of Done (DoD)
- Total Coverage: JUnit5 coverage > 80%.
- Standard Compliance: RFC 7807 is active and validated.
- Architecture Check: Strict layering and DTO patterns are verified.
- Validation: Code passes Maven build and tests.
