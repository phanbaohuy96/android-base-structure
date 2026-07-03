# Onboarding

1. Read `README.md`.
2. Read `AGENTS.md`.
3. Read `CONTEXT.md`.
4. Run `./gradlew --version`.
5. Run `./gradlew :app:assembleDevDebug`.
6. Use `.agents/INDEX.md` to choose task-specific skills.
7. For feature UI work, start in `app/src/main/kotlin/com/pbh/androidbase/feature`.
8. For data work, follow `:domain` repository ports before editing `:data`.
9. For generated-code issues, change annotations or Gradle config and regenerate through Gradle.
10. For boundary checks, run `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`.
11. Prefer `:app:testDevDebugUnitTest`, `:data:compileDevDebugKotlin`, or `:domain:test` before full builds.
12. Run `./gradlew detekt spotlessCheck` before handing off broad or cross-layer changes.
13. Keep secrets, signing files, `local.properties`, and generated output out of edits.
14. Record durable architecture decisions in `docs/adr/`.
