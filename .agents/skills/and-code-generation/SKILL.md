---
name: and-code-generation
description: Work safely with KSP, Hilt, Room, and BuildConfig.
---

## When to use

Use for annotation-processing errors, generated output drift, Hilt component failures, Room DAO/database changes, BuildConfig fields, or KSP version changes.

## Kotlin patterns/refs

Generated files under module `build/` directories are read-only diagnostics. Fix the source annotation, Gradle plugin, schema, constructor, or Hilt module that produced them.

KSP must match the Kotlin version from `gradle/libs.versions.toml`. Hilt bindings should be constructor-injected when possible, `@Binds` for interfaces, and `@Provides` only for builders or conditional values. Room changes belong in `:data`.

## Checklist

- Check `libs.versions.toml` before changing Kotlin/KSP/AGP.
- Never hand-edit generated Hilt, Room, KSP, or BuildConfig output.
- For Hilt errors, inspect the missing binding path and fix the owning module.
- For Room errors, verify DAO signatures, entity annotations, database version, and migrations.
- For BuildConfig fields, define them in the Android module flavor/defaultConfig that consumes them.
- Rerun the exact failing task after each source fix.

## Common mistakes

- Adding `kapt` to a KSP project.
- Creating a binding in a feature package for an app-wide implementation.
- Fixing generated Java instead of Kotlin source.
- Changing Kotlin without updating KSP.

## Verification

- `./gradlew :app:kspDevDebugKotlin`
- `./gradlew :data:kspDevDebugKotlin`
- `./gradlew :app:assembleDevDebug`

## Related

`and-dependency-injection`, `and-data-layer`, `and-module-scaffold`.
