# Android Base Structure Agent Guide

## Identity

This repo is a clone-to-start Android template using Kotlin, Jetpack Compose, Material 3, MVVM + UDF, Hilt, Retrofit, Room, DataStore, and type-safe Navigation Compose.

Base package: `com.pbh.androidbase`.

## Module Graph

Allowed dependencies:

```text
:app -> :core, :data, :domain
:data -> :core, :domain
:core -> :domain
:domain -> Kotlin/JVM only
```

Layer rules:
- `:domain` has no Android imports.
- Feature packages live under `app/src/main/kotlin/com/pbh/androidbase/feature/**`.
- Feature packages never import `com.pbh.androidbase.data.*`.
- `:data` implements repository ports from `:domain`.
- `:app` is the composition root and owns feature navigation plus app-level wiring.
- Feature docs live in `docs/features/`; new features should add
  `docs/features/<name>.md` with a Mermaid workflow.

## Architecture

Use MVVM + unidirectional data flow:
- Screen sends events to a `ViewModel`.
- Feature `ViewModel`s extend `BaseViewModel<S, E>`.
- `BaseViewModel` exposes immutable `state: StateFlow<S>` and `effects: Flow<E>`.
- UI states stay explicit and exhaustive with sealed interfaces.
- One-off events implement `UiEffect`; snackbar/message effects implement `MessageEffect`.
- Screens render through `BaseScreen` unless a lower-level scaffold is genuinely needed.
- Default domain-error copy comes from `DomainError.toUiText()` in `:core`.
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
10. Keep IO, mapping, and persistence outside feature packages.
11. Localize user-facing strings with `strings.xml` when moving beyond prototypes.
12. Do not hide security-sensitive changes in broad refactors.

## Code Generation

Hilt, Room, KSP, and BuildConfig output must be regenerated through Gradle. Do not edit generated output under `build/`.

When adding Hilt bindings, keep app-wide composition in `:app`, repository bindings in `:data`, and avoid feature-package bindings for shared implementations.

## Localization

User-facing text belongs in `res/values/strings.xml` and matching `values-vi/strings.xml` for the module that owns the UI or seed content. ViewModels should expose `UiText.Resource`, not raw strings.

Domain errors remain typed in `:domain`; translate them through `DomainError.toUiText()` or a deliberate feature-specific override at the UI edge.

## Gradle Commands

Priority order:
- `./gradlew :app:assembleDevDebug`
- `./gradlew testDevDebugUnitTest`
- `./gradlew detekt spotlessCheck`
- `./gradlew spotlessApply`

## Security

Never commit secrets, signing keys, Firebase config, `local.properties`, or environment files. Use `keystore.properties.example` as documentation only.
