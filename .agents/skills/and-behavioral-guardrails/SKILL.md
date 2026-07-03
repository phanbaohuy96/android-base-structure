---
name: and-behavioral-guardrails
description: Repository behavior and safety guardrails.
---

## When to use

Use before broad changes, destructive actions, security-sensitive edits, Gradle/build rewrites, or work that touches generated/sensitive paths.

## Kotlin patterns/refs

Prefer small source edits, then verify the exact behavior touched. Read the local code before writing. Generated files under `build/`, KSP/Hilt/Room outputs, signing files, `local.properties`, `.env*`, and `google-services.json` are not source of truth.

Use `rg`/`rg --files` for discovery. Run shell commands through `rtk` in this repo.

## Checklist

- Confirm the touched module and layer boundary before editing.
- Leave unrelated user changes alone.
- Do not edit generated output or secrets.
- Keep behavior changes explicit; avoid hiding them inside cleanup.
- Prefer fail-loud errors over silent fallback for template-critical behavior.
- Verify with the narrowest useful Gradle task, then broaden before final handoff.

## Common mistakes

- Reformatting unrelated files while fixing one issue.
- Patching generated Hilt/Room/KSP classes instead of source annotations.
- Adding broad dependencies to make compilation pass without checking the module graph.
- Treating warning cleanup as permission to change app behavior.

## Verification

- `./gradlew :app:assembleDevDebug`
- `./gradlew detekt spotlessCheck`
- `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`

## Related

`and-code-generation`, `and-module-scaffold`, `and-dependency-injection`.
