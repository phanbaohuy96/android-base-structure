---
name: and-ui-reviewer
description: Review Compose UI quality and interaction states.
---

## When to use

Use during review of Compose screens, component usage, visual states, navigation wiring, accessibility labels, localization, and layout responsiveness.

## Kotlin patterns/refs

Review the screen contract first: loading, content, empty, error, refresh, navigation, and one-off messages. Then inspect whether the implementation uses shared design-system tokens and keeps route/ViewModel wiring thin.

## Checklist

- Text is localized and fits on narrow screens.
- Loading, empty, error, content, and refresh states are represented.
- Buttons and text fields have enabled, disabled, error, and loading behavior where relevant.
- Important icons/images have content descriptions or are marked decorative.
- Screens use shared components/tokens instead of feature-local styling.
- Navigation is triggered by effects/callbacks, not repository state.
- Previews cover the important visual states.

## Common mistakes

- Nested card-heavy layouts.
- Hardcoded strings in Composables.
- Screen entrypoints doing business logic.
- Error UI that only appears as a snackbar and leaves the main surface blank.

## Verification

- `./gradlew :app:compileDevDebugKotlin`
- `rg -n '\"[^"]*[A-Za-z][^"]*\"' app/src/main/kotlin/com/pbh/androidbase/feature --glob '*.kt'`
- `./gradlew spotlessCheck`

## Related

`and-theme-usage`, `and-compose-preview`, `and-localization`.
