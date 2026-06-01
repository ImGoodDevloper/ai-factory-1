# Gemini Agent Interaction Rules

This file defines the core interaction principles and operational boundaries for all Gemini sub-agents within the Knowledge Encyclopedia System (KES).

## 1. Documentation Modification Rule
- **Sub-agent Exclusivity**: ALL project files, documentation, and specifications can ONLY be modified by authorized specialized sub-agents (`sa`, `backend-eng`, `frontend-eng`, `arch-devops`, `web-tester`). 
- **No Direct Intervention**: The system must not bypass sub-agents to make direct modifications to the codebase or documentation without routing through the designated agent's scope.

## 2. Unclear Assignment & Routing Principle
- **Default to SA**: If there is any ambiguity, uncertainty, or lack of clear ownership regarding which sub-agent should handle a specific request or push the project forward, the task **MUST** be routed immediately to the System Analyst agent: **`@sa`**.
- **SA Responsibility**: The `@sa` agent will analyze the requirement, update the project state, and perform the necessary task distribution via `task-assignment.md`.

## 3. Orientation & Triage Protocol
Before executing any assignment, the active agent must run the following verification pipeline:

1. **Plan Validation**: Cross-reference `PROGRESS.log`, `README.md`, and the active codebase.
   - **Missing Items**: If `README.md` defines milestones or requirements that are missing from `PROGRESS.log`, append them to `PROGRESS.log` immediately.
   - **Desync Correction**: If `PROGRESS.log` marks an item as completed `[x]` but the codebase implementation is missing or incomplete, uncheck the item to `[ ]`.

2. **Status Evaluation**:
   - **Project Completion**: If and only if ALL items in the implementation plan are successfully verified and checked `[x]`, AND there are no remaining or unresolved problems listed in `PROBLEM.log`, AND exists `[DRAFT_COMPLETE]`::
     - Output the exact system string: `"🏆 PROJECT_COMPLETE"`
     - Touch and create a blank termination token file named `[MISSION_COMPLETE]` in the root directory.

3. **Task Cleanup**:
   - **JSON Object Removal**: Once the assigned task is successfully executed and completed, the active agent MUST remove its corresponding JSON object from task-assignment.md to keep the active task queue clean.
   - **Problem Log Clearing**: If the task execution successfully resolves the issues documented in PROBLEM.log, the active agent MUST also clear the contents of PROBLEM.log to reflect that no active blockers remain.
