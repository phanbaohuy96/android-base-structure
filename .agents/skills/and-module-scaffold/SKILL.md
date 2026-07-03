---
name: and-module-scaffold
description: Add or update Android modules without breaking the layer graph.
---

## When to use

Use for new modules, module moves, package moves, Gradle dependency changes, or changes to the template module graph.

## Kotlin patterns/refs

This template uses layer-based modules:

`:app -> :feature-auth/:feature-home/:data/:core/:domain`
`:feature-* -> :core -> :domain`
`:data -> :core -> :domain`
`:domain -> pure Kotlin/JVM`

Feature modules are Android libraries with Compose. `:domain` is Kotlin/JVM only. Data implementations stay in `:data`.

## Checklist

- Update `settings.gradle.kts`.
- Use namespace/package under `com.pbh.androidbase`.
- Add only allowed project dependencies.
- Keep plugin selection minimal for the module type.
- Add resource files for user-visible text owned by the module.
- Add tests in the module that owns the behavior.
- Update `README.md`/agent docs if the graph changes.

## Common mistakes

- Adding `:data` to a feature module.
- Adding Android imports to `:domain`.
- Putting shared UI in a feature instead of `:core`.
- Creating a module before checking whether an existing layer owns the concept.

## Verification

- `rtk ./gradlew projects`
- `rtk ./gradlew :new-module:compileDebugKotlin` or `:new-module:test`
- `rtk .agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`

## Related

`and-dependency-injection`, `and-code-generation`, `and-testing`.
