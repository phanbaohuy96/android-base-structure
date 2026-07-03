# Android Base Structure

Read `AGENTS.md` first. It is the tool-neutral source of truth for this template.

Hard rules:
- Do not edit this template unless the user explicitly asks for changes.
- Do not commit unless the user explicitly asks.
- Never commit `google-services.json`, `*.jks`, `*.keystore`, `keystore.properties`, `local.properties`, or `.env*`.
- Prefer `./gradlew` tasks over IDE-only actions.
- Never hand-edit generated Hilt, Room, KSP, or `build/` output.
