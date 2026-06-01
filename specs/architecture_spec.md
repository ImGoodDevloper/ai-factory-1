# Architecture & DevOps System Specification: Knowledge Encyclopedia (KES)

## 1.0 Global Definitions & Roadmap
- **Exposure**: $\text{Expose}(x) \iff \text{Output}(x) \in \text{PublicAPI}$.
- **Diagnostic Source**: $\text{Source}(E) = \text{Access}(\text{LogStorage}, \text{trace\_id}) \mid \text{Level} \geq \text{INTERNAL\_ADMIN}$.
- **Roadmap**: See [ROADMAP.md](./ROADMAP.md) for the implementation sequence $\Phi$.

## 1.1 System Definition ($\mathcal{S}$)
- **$\mathcal{I}$ (Infrastructure)**: $\{ \text{Docker, Nginx, Persistence Layer} \}$
- **$\mathcal{M}$ (Media Storage)**: $\{ \text{Nginx-served Volume} \}$. Images are stored on a persistent volume served by Nginx to ensure isolation from application logic and facilitate future cloud (S3) migration.

## 1.2 Operational Protocols ($\mathcal{P}$)
- **[P-02] Context Integrity**:
    - $Action(t) \implies \exists \text{Search}(\text{PROJECT\_SKELETON.md}) \wedge \text{Read}(\text{docs/README.md})$
- **[P-03] Infrastructure Parity**:
    - $\text{Type}(t) \in \{ \text{Docker, Build} \} \implies \text{Sync}(\text{ENV} \to \{ \text{application.yml}, \text{.env.production} \})$
    - $\text{Map}(\text{Volumes} \leftrightarrow \text{Persistence})$
- **[P-04] Build Reliability**:
    - $\text{Type}(t) = \text{DockerBuild} \implies \text{Apply}(\text{LayerCaching} \wedge \text{OfflineMode} \wedge \text{ContextLocalIgnore})$
    - **ContextLocalIgnore**: $\forall s \in \text{Services}, \text{Context}(s) \neq \text{Root} \implies \exists \text{.dockerignore} \in \text{Context}(s) \mid \text{Exclude}(\text{node\_modules}, \text{target})$. This prevents "invalid file request" due to host-path symlinks (e.g., `node_modules/.bin`).
    - $\text{Type}(t) = \text{Orchestration} \implies \text{Apply}(\text{HealthCheck} \wedge \text{ServiceHealthyDependency})$

## 1.3 Technical Constraints ($\mathcal{C}$)
5. **Ephemerality**: $\forall i \in \text{Images}, \text{Stateless}(i)$. All state must reside in $\mathcal{I}_{persistence}$ or $\mathcal{M}$.
6. **Discovery**: $\text{Connect}(\mathcal{F} \to \mathcal{B}) \implies \text{Target}(\text{Service\_Name}) \notin \{ \text{localhost, 127.0.0.1} \}$.
    - **PrefixPreservation**: $\text{Proxy}(\text{Nginx}) \implies \text{Pass}(\text{FullURI})$. Ensure `proxy_pass` does not strip required prefixes (e.g., use `http://api:8080` without a trailing slash for `/api/` locations).
    - **MediaRouting**: Nginx routes `/media/**` directly to the persistence volume.
7. **Encapsulation & Storage**: 
    - $\text{Container}(s) \implies \text{Isolated}(\text{HostPath})$.
    - $\text{Path}(\text{Persistence\_Storage}) = /app/data$ (Runtime).
    - **Path(Media_Storage)**: `/app/media` (Nginx Runtime).
    - **CloudScalability**: Media storage architecture must remain decoupled via URI abstraction to support migration to object storage without frontend modification.
    - $\text{Path}(\text{Frontend\_Cache}) = /app/cache$ (Runtime).
9. **Cache Architecture**:
    - **Build-time** (${GEMINI\_CACHE\_ROOT}$): Exists in Host/CI, used for compilation. MUST NOT be included in final images.
    - **Runtime** (/app/cache): Internal to container, used for active application acceleration.
    - **Invariant**: $\text{Cache}(\text{Build}) \cap \text{Cache}(\text{Runtime}) = \emptyset$.
11. **SafetyGuards**: $\forall d \in \text{DestructiveActions}, \text{Trigger}(d) \implies \text{UserConfirmation} \wedge \text{ServerSideValidation}$.

## 3.1 Definition of Done ($\mathcal{D}o\mathcal{D}$)
A task $t$ is terminal if and only if $\text{Done}(t) = \text{True}$:
- $\text{Validated}(\text{MultiStageBuild}) \wedge \text{HealthCheck}(\text{Docker})$

## 3.2 Self-Healing & Hygiene
- **Self-Healing ($\mathcal{H}$)**: $\text{Heal}(E) = \text{Analyze}(\text{Source}(E)) \to \text{Verify}(\text{Env}) \to \text{Refactor}(\text{Code}) \to \text{Update}(\text{Tests})$.
- **Privacy**: Public API responses must never contain StackTrace.
- **Hygiene**: $\mathcal{S}_{final} \implies \text{Update}(\text{PROGRESS.log})$. Language: Technical English / Traditional Chinese.
