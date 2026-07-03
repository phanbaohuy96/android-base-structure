---
name: and-viewmodel-pattern
description: Build MVVM + UDF ViewModels and screen states.
---

## When to use

Use for Compose screens, ViewModels, UiState, UiEvent/Intent, one-off effects, loading/error state, or navigation effect changes.

## Kotlin patterns/refs

Expose immutable `StateFlow<UiState>` with a private `MutableStateFlow`. Model renderable screen state as a sealed interface or data class. Model transient work such as snackbars and navigation as sealed `Effect` values sent through a `Channel` and exposed with `receiveAsFlow()`.

ViewModels call use cases, not repositories. Composables render state and send events/callbacks; they do not validate business rules or map transport errors.

## Checklist

- Initial state is explicit and renderable.
- State contains data needed to draw the screen, not one-off commands.
- Effects are one-off and collected with lifecycle-aware helpers.
- Validation happens in use cases; ViewModels map errors to `UiText`.
- Public state/effect flows are immutable.
- Long-running work guards duplicate submits or exposes refreshing/loading state.
- Tests cover success, failure, and effect emission.

## Common mistakes

- Exposing `MutableStateFlow` publicly.
- Storing navigation as persistent UiState.
- Calling data repositories directly from feature ViewModels.
- Emitting raw `String` messages from ViewModels.
- Leaving loading state stuck after failure.

## Verification

- `rtk ./gradlew :feature-auth:testDebugUnitTest :feature-home:testDebugUnitTest`
- `rtk ./gradlew :domain:test`
- `rtk rg -n "MutableStateFlow|Channel|UiText|Repository" feature-auth/src/main feature-home/src/main --glob '*.kt'`

## Related

`and-error-handling`, `and-testing`, `and-navigation`.
