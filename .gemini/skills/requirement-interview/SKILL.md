---
name: requirement-interview
description: Standardized requirement gathering and scoping process. Use this skill whenever a user provides a new task, feature request, or complex instruction to ensure full alignment and minimize rework. This skill enforces a 4-step interview process before execution.
---

# Requirement Interview (需求訪談)

## Overview

This skill ensures that the AI agent fully understands the user's intent and project constraints before committing any changes. It prevents premature execution and promotes high-signal communication through a structured interview workflow.

## The 4-Step Interview Workflow

Whenever a new requirement is presented, you **MUST** follow these steps and then **STOP** to wait for user confirmation. Do not produce the final implementation until instructed.

### 1. Understanding Summary (理解摘要)
Provide a two-sentence summary of your understanding of the task's goal and the intended audience/users.

### 2. Clarifying Questions (待釐清問題)
List at least 3 critical points where information is insufficient or where you need technical/architectural confirmation. Focus on:
- Edge cases
- Integration points
- UI/UX preferences
- Security or performance constraints

### 3. Action Plan (行動指南)
Outline the technical approach, including:
- Execution steps (step-by-step)
- Logical architecture or patterns to be used
- Specific tools (CLI, libraries, or sub-agents) required

### 4. Pause for Confirmation (請求確認)
Explicitly state that you are pausing and waiting for the user to confirm the plan or answer the clarifying questions before proceeding.

## Example Usage

**User Request**: "Add a login page to the frontend."

**Agent Response (using skill)**:
> ### 1. 理解摘要
> 本任務旨在為前端應用新增登錄頁面，以實現使用者驗證與存取控制。目標對象為系統管理員與註冊使用者。
> 
> ### 2. 待釐清問題
> 1. 您偏好的身份驗證機制為何（例如：JWT, OAuth2, 或簡單的 Session）？
> 2. 登錄頁面的視覺風格是否需要與現有的 `style.css` 嚴格一致，還是有特定的 UI 框架需求？
> 3. 是否需要實作「忘記密碼」或「第三方登錄」（如 Google）的功能？
> 
> ### 3. 行動指南
> 1. 在 `src/views` 建立 `Login.vue` 並更新 `router/index.ts`。
> 2. 在 `pageStore.ts` 擴充 `userAuth` 狀態管理。
> 3. 使用 `axios` 對接後端的 `/api/auth/login` 端點（需先確認後端是否已具備此介面）。
> 
> ### 4. 請求確認
> 我已暫停作業。請確認上述計劃並回覆釐清問題，我將在收到您的回覆後開始實作。

---

## 資源 (Resources)

### references/
- **`best_practices.md`**: Guidelines for effective requirement gathering and software scoping.
