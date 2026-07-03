---
name: and-module-scaffold
description: Add app feature packages or update Android modules without breaking the layer graph.
---

## When to use

Use for new app feature packages, module moves, package moves, Gradle dependency changes, or changes to the template module graph.

## Kotlin patterns/refs

This template uses layer-based modules:

`:app -> :core/:data/:domain`
`:data -> :core -> :domain`
`:domain -> pure Kotlin/JVM`

Feature code lives as packages under `:app` at `com.pbh.androidbase.feature.*`. `:domain` is Kotlin/JVM only. Data implementations stay in `:data`.

## Checklist

- For new features, scaffold a package under `app/src/main/kotlin/com/pbh/androidbase/feature/<name>/`.
- Update `settings.gradle.kts` only when the Gradle module graph changes.
- Use namespace/package under `com.pbh.androidbase`.
- Add only allowed project dependencies.
- Keep plugin selection minimal for the module type.
- Add user-visible feature strings to `app/src/main/res/values*/strings.xml`.
- Add feature tests under `app/src/test/kotlin/com/pbh/androidbase/feature/<name>/`.
- Update `README.md`/agent docs if the graph changes.

## Common mistakes

- Creating a feature Gradle module when an app feature package is enough.
- Importing `com.pbh.androidbase.data.*` from a feature package.
- Adding Android imports to `:domain`.
- Putting shared UI in a feature instead of `:core`.
- Creating a module before checking whether an existing layer owns the concept.

## Verification

- `./gradlew projects`
- `./gradlew :app:compileDevDebugKotlin` for feature packages, or `:new-module:compileDebugKotlin` for real modules
- `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`

## Related

`and-dependency-injection`, `and-code-generation`, `and-testing`.
