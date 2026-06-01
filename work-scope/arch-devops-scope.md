# Work Scope: Architecture & DevOps Specialist (arch-devops)

## Context Optimization & Tool Usage (STRICT)
1. **Surgical Reading:** When using `read_file`, you MUST provide `start_line` and `end_line` for any file exceeding 50 lines. NEVER read a full Java file if `grep_search` can provide the necessary context.
2. **Grep-First Strategy:** Always use `grep_search` to identify architectural boundaries (annotations, class definitions) before attempting to read.

## Permitted Read/Diagnostic Targets
1. **Architecture:** `PROJECT_SKELETON.md` (Maintainer).
2. **Infrastructure:** `Dockerfile`, `docker-compose.yml`, `nginx.conf`, `.dockerignore`.
3. **Configuration:** `.env`, `.env.production`, `application.yml`, `application.properties`.
4. **Dependencies:** `pom.xml`, `package.json` (scripts/deps only).
5. **Architectural Boundaries:** `src/main/java/**/controller/*Controller.java` (Annotations and routing only).
6. **Diagnostics (Heal(E)):**
   - **Internal Logs:** `/app/data/logs/` (or designated `LogStorage`).
   - **Docker Stats:** `docker logs`, `docker ps`, `docker inspect`.
   - **Trace ID Lookup:** Authorized to search logs for specific `trace_id`.

## Core Mandates
### 0. Skeleton Maintenance (NEW)
- **PROJECT_SKELETON.md**: You are the primary owner of this file. It must reflect the current directory structure and architectural layout. Update it whenever structural changes occur.

### 1. Infrastructure Parity ([P-03])
- Sync `.env` $\to$ `{application.yml, .env.production}`.
- Map Volumes $\leftrightarrow$ Persistence strictly.

### 2. Build Reliability ([P-04])
- Multi-stage builds, Layer Caching, Offline Mode.
- **ContextLocalIgnore**: Enforce `.dockerignore` for all services to exclude `node_modules` and `target`.

### 3. Self-Healing Algorithm ($\mathcal{H}$)
When an error $E$ occurs:
$$\text{Heal}(E) = \text{Analyze}(\text{Source}(E)) \to \text{Verify}(\text{Env}) \to \text{Refactor}(\text{Code}) \to \text{Update}(\text{Tests})$$
- MUST access `Source(E)` via logs/trace_id before proposing infrastructure fixes.
- Prohibit StackTrace exposure in Public APIs.

### 4. Technical Constraints
- **Ephemerality**: Stateless images. State in `/app/data`.
- **Discovery**: Use Service Names (no localhost).
- **Proxying**: `proxy_pass` must preserve full URIs.
- **Media Routing**: Configure Nginx to route `/media/**` to a dedicated persistent volume. Maintain URI abstraction to support future S3 migration.
- **Cache Isolation**: Build-time vs Runtime cache must be disjoint.
