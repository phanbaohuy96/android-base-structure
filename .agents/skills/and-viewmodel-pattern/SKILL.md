---
name: and-viewmodel-pattern
description: Build MVVM + UDF ViewModels and screen states.
---

## When to use

Use for Compose screens, ViewModels, UiState, UiEvent/Intent, one-off effects, loading/error state, or navigation effect changes.

## Kotlin patterns/refs

Feature ViewModels extend `BaseViewModel<S, E>` from `:core`. Model renderable screen state as a sealed interface or data class and pass the initial state to `BaseViewModel`. Mutate state only with `setState { ... }`; emit one-off effects only with `sendEffect(...)`.

Model transient work such as snackbars and navigation as sealed `UiEffect` values. Message/snackbar cases implement `MessageEffect` and carry `UiText`; `BaseScreen` shows them automatically. Screens should render through `BaseScreen` and handle only feature-specific effects such as navigation in `onEffect`.

ViewModels call use cases, not repositories. Composables render state and send events/callbacks; they do not validate business rules or map transport errors.

## Checklist

- Initial state is explicit and renderable.
- State contains data needed to draw the screen, not one-off commands.
- Effects are one-off sealed `UiEffect` values.
- Validation happens in use cases; ViewModels map domain errors with `DomainError.toUiText()` unless a feature-specific override is deliberate.
- Public state/effect flows come from `BaseViewModel`.
- Screens use `BaseScreen` unless they need a clearly different scaffold.
- Long-running work guards duplicate submits or exposes refreshing/loading state.
- Tests cover success, failure, and effect emission.

## Common mistakes

- Exposing `MutableStateFlow` publicly.
- Storing navigation as persistent UiState.
- Calling data repositories directly from feature ViewModels.
- Emitting raw `String` messages from ViewModels.
- Hand-rolling `Channel`/`MutableStateFlow` plumbing instead of using `BaseViewModel`.
- Leaving loading state stuck after failure.

## Verification

- `./gradlew :app:testDevDebugUnitTest`
- `./gradlew :domain:test`
- `rg -n "MutableStateFlow|Channel|Repository" app/src/main/kotlin/com/pbh/androidbase/feature --glob '*.kt'`

## Related

`and-error-handling`, `and-testing`, `and-navigation`.
