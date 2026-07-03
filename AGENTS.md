# Android Base Structure Agent Guide

## Identity

This repo is a clone-to-start Android template using Kotlin, Jetpack Compose, Material 3, MVVM + UDF, Hilt, Retrofit, Room, DataStore, and type-safe Navigation Compose.

Base package: `com.pbh.androidbase`.

## Module Graph

Allowed dependencies:

```text
:app -> :feature-auth, :feature-home, :data, :core, :domain
:feature-auth -> :core, :domain
:feature-home -> :core, :domain
:data -> :core, :domain
:core -> :domain
:domain -> Kotlin/JVM only
```

Layer rules:
- `:domain` has no Android imports.
- `:feature-*` modules never depend on or import `:data`.
- `:data` implements repository ports from `:domain`.
- `:app` is the composition root and owns app-level wiring.

## Architecture

Use MVVM + unidirectional data flow:
- Screen sends events to a `ViewModel`.
- `ViewModel` exposes `StateFlow<UiState>`.
- One-off events use `Channel` exposed as `Flow`.
- `UseCase` classes orchestrate domain behavior.
- Repository interfaces live in `:domain`; implementations live in `:data`.

## Operating Principles

1. Read before writing.
2. Think through the smallest coherent change.
3. Preserve the module graph.
4. Prefer simple Kotlin over framework cleverness.
5. Keep generated code generated.
6. Fail loud with useful errors.
7. Verify with the narrowest meaningful Gradle task first.
8. Match local naming, package, and file conventions.
9. Keep UI state explicit and exhaustive with sealed interfaces.
10. Keep IO, mapping, and persistence outside feature modules.
11. Localize user-facing strings with `strings.xml` when moving beyond prototypes.
12. Do not hide security-sensitive changes in broad refactors.

## Gradle Commands

Priority order:
- `./gradlew :app:assembleDevDebug`
- `./gradlew testDevDebugUnitTest`
- `./gradlew detekt spotlessCheck`
- `./gradlew spotlessApply`

## Code Generation

Hilt, Room, KSP, and BuildConfig output must be regenerated through Gradle. Do not edit generated output under `build/`.

## Security

Never commit secrets, signing keys, Firebase config, `local.properties`, or environment files. Use `keystore.properties.example` as documentation only.
