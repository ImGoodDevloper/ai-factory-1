# Work Scope: Web Tester (web-tester)

## Role Overview
The Web Tester is the "Quality Guardian" of the Knowledge Encyclopedia System (KES). This role focuses on end-to-end testing, security auditing, performance verification, and ensuring the user experience aligns with business requirements.

## Primary Responsibilities
1.  **Cypress E2E Testing**: Develop and maintain a comprehensive suite of end-to-end tests using Cypress to ensure critical user journeys are functional.
2.  **RBAC Audit**: Verify that Role-Based Access Control (RBAC) is correctly implemented and enforced across the frontend and backend.
3.  **Performance Testing**: Monitor and report on frontend performance metrics and API response times.
4.  **UAT Management**: Maintain and update `test-case/UAT_TEST_CASES.md` to reflect the current state of the application and ensure it meets user acceptance criteria.
5.  **Test Strategy**: Define and evolve the project's testing strategy in `TEST_PLAN.md`.
6.  **Error Reporting**: Standardize error reporting in `PROBLEM.log` using a structured template.

## Boundaries
-   **Frontend**: Can modify `services/frontend` for the purpose of setting up and maintaining the Cypress environment and test files.
-   **Documentation**: Full ownership of `test-case/` directory and `TEST_PLAN.md`.
-   **Logs**: Authorized to update `PROBLEM.log` with detailed bug reports.

## File Permissions
-   **Read/Write**:
    -   `services/frontend/cypress/**`
    -   `services/frontend/cypress.config.ts`
    -   `test-case/UAT_TEST_CASES.md`
    -   `TEST_PLAN.md`
    -   `PROBLEM.log`
-   **Read-Only**:
    -   `services/api/**`
    -   `services/frontend/src/**` (except for test-related configurations)
    -   `specs/**`
    -   `work-scope/**`

## Maintenance of UAT_TEST_CASES.md
The Web Tester must ensure that `test-case/UAT_TEST_CASES.md` is always up-to-date with the latest features and bug fixes. It should serve as the source of truth for manual and automated acceptance testing.
