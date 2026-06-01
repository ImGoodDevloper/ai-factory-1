# Project Skeleton

This file maps the current directory structure and architectural layout of the Knowledge Encyclopedia System (KES). It is maintained by the `arch-devops` agent.

## Root Directory
- `.gemini/`: Agent configurations and profile instructions.
  - `agents/`: Specialized agent system prompts and instructions.
  - `hooks/`: Automation hooks for agent operations.
  - `skills/`: Reusable skill definitions for agents.
  - `settings.json`: Global agent settings.
- `specs/`: Project specifications and global requirements.
  - `ROADMAP.md`: Project development phases and milestones.
  - `architecture_spec.md`: Core system architecture and design guidelines.
  - `backend_spec.md`: Backend-specific technical requirements and API design.
  - `frontend_spec.md`: Frontend-specific technical requirements and UI/UX design.
  - `TEST_PLAN.md`: Comprehensive testing strategy and coverage goals.
- `work-scope/`: Detailed operational scopes for each specialized agent (`sa-scope.md`, `web-tester-scope.md`, etc.).
- `services/`:
  - `api/`: Spring Boot backend service.
    - `pom.xml`: Maven project configuration.
    - `Dockerfile`: Multi-stage build for API.
    - `.dockerignore`: API-specific ignore rules.
    - `src/main/java/com/kes/api/`:
      - `config/`: Security (JWT), Web configuration, and Data initialization.
      - `controller/`: REST API controllers (Auth, Page, Media, AuditLog).
      - `dto/`: Data Transfer Objects for API requests/responses.
      - `entity/`: JPA entities (User, Role, Page, AuditLog).
      - `exception/`: Custom exceptions and global exception handler.
      - `repository/`: Spring Data JPA repositories.
      - `service/`: Business logic implementation.
      - `KesApiApplication.java`: Main application entry point.
    - `src/main/resources/`:
      - `application.yml`: Backend configuration.
    - `src/test/`: Integration and unit tests.
  - `frontend/`: Vue.js frontend service.
    - `package.json`: Node.js project configuration.
    - `vite.config.ts`: Vite build and dev server configuration.
    - `Dockerfile`: Multi-stage build for Frontend.
    - `nginx.conf`: SPA routing configuration for frontend.
    - `.dockerignore`: Frontend-specific ignore rules.
    - `src/`:
      - `api/`: Axios client and API service definitions.
      - `assets/`: Global styles and static assets.
      - `components/`: Reusable UI components (Editor, Search, Modals) and their `__tests__`.
      - `router/`: Vue Router navigation guards and routes.
      - `stores/`: Pinia state management (Auth, Page, Notification) and their `__tests__`.
      - `types/`: TypeScript type definitions and interfaces.
      - `utils/`: Helper functions and environment utilities.
      - `views/`: Main page views (Home, Login, Page, AuditLog).
      - `App.vue`: Root component.
      - `main.ts`: Application entry point.
    - `cypress/`: E2E testing suite (Smoke and UAT tests).
- `infrastructure/`:
  - `persistence/`: Database storage (H2).
  - `nginx/`: Nginx configuration.
    - `nginx.conf`: Reverse proxy and media routing configuration.
- `data/`: Persistent data storage (local development).
  - `h2/`: H2 database files.
  - `media/`: Media storage volume.
  - `logs/`: Application logs.
- `scripts/`: Utility scripts for development, deployment, and maintenance.
- `test-case/`: User Acceptance Test (UAT) documentation.
  - `UAT_TEST_CASES.md`: Comprehensive functional test cases.
- `PROGRESS.log`: Append-only activity record tracking development history.
- `PROBLEM.log`: Active blockers and issues tracking.
- `task-assignment.md`: Current task distribution state, structured in JSON format.
- `PROJECT_SKELETON.md`: This file.
- `README.md`: Project overview and quick start guide.
- `GEMINI.md`: Core interaction principles and operational boundaries for agents.
- `docker-compose.yml`: Orchestration for all services.
- `.env`: Environment variables.
- `.dockerignore`: Root-level Docker ignore rules.
- `.gitignore`: Git ignore rules.
- `.gitattributes`: Git attribute configuration.
- `[DRAFT_COMPLETE]`: Token indicating the draft phase is complete.
- `[MISSION_COMPLETE]`: Token indicating the entire mission is complete.

## Agents
- `sa`: System Analyst, responsible for project direction and requirements.
- `backend-eng`: Backend Engineer, responsible for Java/Spring Boot API.
- `frontend-eng`: Frontend Engineer, responsible for Vue.js application.
- `editor-eng`: Editor Specialist, responsible for Markdown and rich text editing features.
- `frontend-infra`: Frontend Infrastructure, responsible for build tools, CI/CD, and performance.
- `ui-ux-eng`: UI/UX Engineer, responsible for design system and user experience.
- `arch-devops`: Architecture & DevOps, responsible for infrastructure and CI/CD.
- `web-tester`: Quality Guardian, responsible for Cypress E2E, RBAC audit, and UAT.
- `work-consultant`: Project management and workflow optimization.
- `pure-consultant`: General technical advisory.

## Architectural Layout
The Knowledge Encyclopedia System (KES) follows a containerized microservices-lite architecture:

1.  **Reverse Proxy (Nginx)**: Acts as the single entry point (Port 80).
    - Routes `/api/**` to the Backend service.
    - Routes `/media/**` to the persistent media storage volume.
    - Routes all other traffic to the Frontend service.
2.  **Frontend (Vue 3 + Vite)**:
    - Single Page Application (SPA) served via Nginx.
    - Uses Pinia for state management and Vue Router for navigation.
    - Communicates with the Backend via REST API.
3.  **Backend (Spring Boot 3)**:
    - RESTful API providing core business logic (Page management, Media handling, Audit logs).
    - Security handled via Spring Security and JWT.
    - Persistence layer using Spring Data JPA.
4.  **Database (H2)**:
    - Relational database for metadata and content storage.
    - Persistent via file-based storage in the `data/h2` volume.
5.  **Storage**:
    - Local file system volumes for Media and Logs, abstracted for future cloud migration (e.g., S3).
