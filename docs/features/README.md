# Feature Documentation

Feature docs describe the user flow, entry points, state model, and extension points
for each app feature.

- [Authentication](authentication.md)
- [Home](home.md)

New features should follow the base UI tier: ViewModels extend `BaseViewModel`,
screens render through `BaseScreen` when possible, one-off actions implement
`UiEffect`, snackbar effects implement `MessageEffect`, and domain failures are
translated with `DomainError.toUiText()` at the UI edge.
