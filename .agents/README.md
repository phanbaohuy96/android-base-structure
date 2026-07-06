# Agent Assets

This directory contains tool-neutral operating docs and reusable skills for Android work in this template.

Start with `INDEX.md`, then open only the skill that matches the task.
`and-*` skills are project-specific; the other skill directories are upstream Android skills installed from `android/skills`.
Use `AGENTS.md` for hard architecture rules and `CONTEXT.md` for naming.
Skills are intentionally narrow; combine only the ones required by the current change.
Feature work usually happens under `app/src/main/kotlin/com/pbh/androidbase/feature`.
Run the smallest Gradle task that verifies the touched layer before broad checks.
Do not edit generated Hilt, Room, KSP, or BuildConfig output.

Update kept upstream Android skills with:

```bash
rtk bash .agents/scripts/update-android-skills.sh
```

Use `ANDROID_SKILLS_REF=<ref>` to pin a release or commit. The updater skips upstream `testing-setup`, `navigation-3`, and `styles` because their useful guidance is merged into `and-testing`, `and-navigation`, and `and-theme-usage`.
Repo-specific patches for upstream skills live under `.agents/skill-overlays/`
and are reapplied by the updater after each refresh.
