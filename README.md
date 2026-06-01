# Knowledge Encyclopedia System (KES)

## Project Overview
The Knowledge Encyclopedia System (KES) is a sophisticated knowledge management platform designed to support hierarchical page structures with unlimited nesting depth. It features a live Markdown preview, recursive search, and robust security measures.

## Tech Stack
- **Backend**: Java / Spring Boot
- **Frontend**: Vue.js / Pinia
- **Database**: H2 (Persistence)
- **Infrastructure**: Docker / Nginx

## Project Structure
- `services/api/`: Spring Boot backend service.
- `services/frontend/`: Vue.js frontend service.
- `specs/`: Project specifications and roadmap.
- `work-scope/`: Operational boundaries for specialized agents.
- `.gemini/agents/`: Agent profiles and instructions.

## Current Status
The project is in the final verification phase (Phase 7). All core features have been implemented and verified through unit and integration tests. We are currently automating User Acceptance Test (UAT) cases using Cypress to ensure full End-to-End (E2E) coverage.

## Documentation
- [Roadmap](specs/ROADMAP.md)
- [Architecture Specification](specs/architecture_spec.md)
- [Implementation Progress](PROGRESS.log)
- [UAT Test Cases](test-case/UAT_TEST_CASES.md)
