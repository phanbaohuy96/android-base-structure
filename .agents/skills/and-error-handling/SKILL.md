---
name: and-error-handling
description: Map failures into domain errors and user-facing effects.
---

## When to use

Use when adding API calls, persistence, validation, repository behavior, use cases, ViewModel effects, or user-visible failure states.

## Kotlin patterns/refs

Use `AppResult<T>` for fallible domain/data operations. Map transport/storage exceptions to `DomainError` in `:core` or `:data`. Validate inputs in use cases and represent validation as typed reasons, not UI strings.

ViewModels translate `DomainError` into `UiText.Resource` and emit one-off messages through effects.

## Checklist

- Do input validation in use cases, not Composables.
- Keep raw exception messages out of UI.
- Keep `DomainError` free of Android resources and localized text.
- Map network status codes to stable domain errors.
- Emit transient messages as effects, not persistent state unless the screen renders that state.
- Test success, validation failure, transport failure, and unknown failure paths.

## Common mistakes

- Throwing Retrofit exceptions into feature modules.
- Returning a freeform server message directly to UI.
- Encoding navigation as an error state.
- Swallowing failures and leaving loading spinners active.

## Verification

- `rtk ./gradlew :domain:test`
- `rtk ./gradlew :core:testDebugUnitTest`
- `rtk ./gradlew :feature-auth:testDebugUnitTest :feature-home:testDebugUnitTest`

## Related

`and-data-layer`, `and-viewmodel-pattern`, `and-localization`.
