---
name: and-compose-preview
description: Add useful Compose previews.
---

## When to use

Use when adding or changing Compose screens, stateless content components, reusable design-system components, or visible UI states.

## Kotlin patterns/refs

Preview stateless content whenever possible. Keep Hilt-backed route/screen entrypoints thin and preview the inner content function with fake immutable data. Wrap previews in `AndroidBaseTheme`, and show light/dark or key states when the component meaningfully differs.

## Checklist

- Include loading, content, empty, and error states when a screen supports them.
- Keep fake data local to the preview file or a small preview fixture.
- Use localized string resources in production UI; preview-only labels can be local constants.
- Avoid previewing functions that require a real `ViewModel`, `NavController`, network, or database.
- Keep preview parameters stable so Studio renders quickly.

## Common mistakes

- Previewing a Hilt entrypoint directly.
- Adding previews that only cover the happy path.
- Hardcoding production strings to make previews easier.
- Creating fake repository implementations just for a preview.

## Verification

- `rtk ./gradlew :feature-auth:compileDebugKotlin`
- `rtk ./gradlew :feature-home:compileDebugKotlin`
- `rtk ./gradlew spotlessCheck`

## Related

`and-theme-usage`, `and-ui-reviewer`, `and-localization`.
