---
name: and-theme-usage
description: Use shared Compose design system tokens and components.
---

## When to use

Use for screen styling, reusable components, theme changes, typography/shape/color updates, spacing, elevation, or UI consistency work.

## Kotlin patterns/refs

Use `AndroidBaseTheme` and tokens from `:core:designsystem`. Feature packages should compose screens from shared Material 3 tokens and reusable components such as `AppScaffold`, `AppButton`, and `AppTextField`. Theme configuration belongs in core/app, not feature-local forks.

## Checklist

- Prefer `MaterialTheme` and `AppTheme` tokens over hardcoded colors/spacing.
- Keep feature screens visually consistent with shared components.
- Support light/dark mode through `AppThemeDefaults`.
- Keep cards for repeated items or true containers.
- Verify text fits on narrow widths and does not overlap.
- Add or update previews for meaningful visual states.
- Keep user-facing text localized while styling it.

## Common mistakes

- Hardcoding repeated colors or dimensions in feature screens.
- Creating a second feature-specific theme.
- Nesting decorative cards inside cards.
- Styling disabled/loading/error states as afterthoughts.

## Verification

- `./gradlew :core:compileDebugKotlin`
- `./gradlew :app:compileDevDebugKotlin`
- `./gradlew spotlessCheck`

## Related

`and-compose-preview`, `and-ui-reviewer`, `and-localization`.
