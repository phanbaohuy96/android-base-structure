# Agent Assets

This directory contains tool-neutral operating docs and reusable skills for Android work in this template.

Start with `INDEX.md`, then open only the skill that matches the task.
Use `AGENTS.md` for hard architecture rules and `CONTEXT.md` for naming.
Skills are intentionally narrow; combine only the ones required by the current change.
Feature work usually happens under `app/src/main/kotlin/com/pbh/androidbase/feature`.
Run the smallest Gradle task that verifies the touched layer before broad checks.
Do not edit generated Hilt, Room, KSP, or BuildConfig output.
