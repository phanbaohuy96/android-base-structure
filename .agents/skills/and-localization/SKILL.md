---
name: and-localization
description: Keep user-facing strings localizable.
---

## When to use

Use before shipping visible text, validation messages, snackbars, empty/error states, labels, buttons, placeholders, content descriptions, or sample display content.

## Kotlin patterns/refs

Production user-facing strings live in module-local `res/values/strings.xml` and `res/values-vi/strings.xml`. Compose reads text with `stringResource`; ViewModels use `UiText.Resource` so strings resolve in UI or at the Android edge.

Keep `:domain` free of localized strings. Use typed reasons such as `ValidationReason` and translate at the UI edge.

## Checklist

- Add every English key to Vietnamese resources in the same module.
- Use plurals for counts and formatted resources for variable text.
- Do not concatenate translated fragments.
- Keep raw server messages out of long-lived UI.
- Add content descriptions for meaningful icons/images.
- Treat mock seed titles/descriptions as localized if users see them.

## Common mistakes

- Keeping ViewModel messages as `String`.
- Leaving feature-owned UI strings inline instead of adding them to `:app` resources.
- Using English default credentials or labels inline in Composables.
- Forgetting `values-vi` parity after adding a key.

## Verification

- `rg -n '\"[^"]*[A-Za-z][^"]*\"' app/src/main core/src/main data/src/main domain/src/main --glob '*.kt' --glob '!**/build/**'`
- Compare each module's `values/strings.xml` keys with `values-vi/strings.xml`.
- `./gradlew :app:assembleDevDebug`

## Related

`and-theme-usage`, `and-error-handling`, `and-testing`.
