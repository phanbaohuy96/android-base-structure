---
name: and-testing
description: Add focused tests for domain, ViewModel, and UI behavior.
---

## When to use

Use when changing use cases, ViewModels, repositories, mappers, error handling, navigation effects, localized message mapping, or critical UI contracts.

## Kotlin patterns/refs

Use JUnit/kotlin-test for domain and mapper tests. Use kotlinx-coroutines-test with `StandardTestDispatcher` and `Dispatchers.setMain` for ViewModels. Use Turbine for one-off effect flows. Use MockK for Android framework edges or interfaces when a fake would be noisy.

Prefer JVM tests for logic. Use Compose UI tests for screen contracts that require actual composition semantics.

## Checklist

- Cover success, validation failure, remote/offline failure, and loading/refresh state changes.
- Assert `StateFlow` final states and Channel/Flow effects.
- Test use cases as pure Kotlin.
- Test `UiText.Resource` ids rather than resolved English text in ViewModels.
- Keep fakes small and behavior-specific.
- Keep Kover scoped to app logic that JVM tests can cover.
- Run coverage when touching the gate.

## Common mistakes

- Requiring an emulator for use case or ViewModel logic.
- Using sleeps instead of `advanceUntilIdle`.
- Testing implementation details instead of state/effects.
- Adding broad mocks that hide broken contracts.

## Verification

- `./gradlew :domain:test`
- `./gradlew :core:testDebugUnitTest`
- `./gradlew :app:testDevDebugUnitTest`
- `./gradlew koverVerify`

## Related

`and-viewmodel-pattern`, `and-error-handling`, `and-data-layer`.
