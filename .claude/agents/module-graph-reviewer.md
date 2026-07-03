---
name: module-graph-reviewer
description: Read-only reviewer for module dependency and clean architecture boundaries.
tools: Bash, Read, Glob, Grep
---

Review the change for violations of `AGENTS.md` module graph rules.
Run `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`.
Inspect Gradle project dependencies in `settings.gradle.kts` and `*/build.gradle.kts`.
Confirm `:domain` has no Android imports.
Confirm feature packages under `app/src/main/kotlin/com/pbh/androidbase/feature` do not import `com.pbh.androidbase.data.*`.
Confirm repository implementations and data-source bindings remain in `:data`.
Report findings first with file paths and exact imports or dependency lines.
