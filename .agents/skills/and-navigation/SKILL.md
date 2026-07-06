---
name: and-navigation
description: Add type-safe Navigation Compose routes and graphs.
---

## When to use

Use for new destinations, nested graphs, navigation arguments, auth guards, deep links, multiple back stacks, returning results from flows, or route refactors.

## Kotlin patterns/refs

Routes are `@Serializable` objects or data classes. Feature packages expose `fun NavGraphBuilder.featureGraph(...)` from inside `:app` and keep graph-local navigation callbacks explicit. The root `NavHost` and cross-feature aggregation live in `:app`.

Prefer `toRoute<T>()`/typed navigation APIs over raw route strings for new work.

For Navigation 3 migration requests, first verify the current Navigation dependency and migration scope. Keep this template's route ownership rules intact while adopting newer APIs: feature graph entrypoints stay under `:app`, root graph aggregation stays in `:app`, and feature packages should not learn unrelated routes.

## Checklist

- Apply Kotlin serialization plugin where typed routes live.
- Keep route models small and serializable.
- Use stable IDs in route args; load rich objects through use cases.
- Keep root start-destination/auth-guard logic in `:app`.
- Model dialogs, bottom sheets, and multi-pane scenes as explicit graph behavior, not ad hoc screen state.
- Keep deep-link parsing and auth-gated redirect behavior covered by tests.
- Do not store one-off navigation as persistent UiState.
- Test ViewModel effects that trigger navigation.

## Common mistakes

- Passing full domain objects through navigation args.
- Reading route arguments manually when typed APIs are available.
- Making feature packages know about unrelated feature routes.
- Triggering navigation directly from repository callbacks.

## Verification

- `./gradlew :app:compileDevDebugKotlin`
- `./gradlew :app:assembleDevDebug`
- `./gradlew :app:testDevDebugUnitTest`

## Related

`and-viewmodel-pattern`, `and-testing`, `and-module-scaffold`.
