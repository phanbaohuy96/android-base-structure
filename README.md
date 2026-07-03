# Android Base Structure

Modern Android starter template using Kotlin, Compose, Material 3, Hilt, Retrofit, Room, DataStore, and type-safe Navigation Compose.

## Quickstart

```bash
./gradlew :app:assembleDevDebug
./gradlew :app:installDevDebug
```

Mock login is enabled for the `dev` flavor. Use `demo@example.com` and `password`.

## Modules

```text
:app
:core
:data
:domain
:feature-auth
:feature-home
```

Feature modules depend only on `:core` and `:domain`. Data implementations stay in `:data`. The app module composes everything.

## Flavors

- `dev`: mock auth, local HTTP allowed for emulator hostnames.
- `staging`: real API URL placeholder, cleartext disabled.
- `prod`: real API URL placeholder, cleartext disabled.

Override base URL in CI or local builds with:

```bash
./gradlew :app:assembleDevDebug -PANDROID_BASE_URL=https://example.test/
```

## Common Tasks

```bash
./gradlew :app:assembleDevDebug
./gradlew testDevDebugUnitTest
./gradlew detekt spotlessCheck
./gradlew spotlessApply
./gradlew koverVerify
```

The thin `Makefile` wraps these commands for convenience.

## Theme Configuration

The app UI is controlled from `:core` through `AndroidBaseTheme` and `AppThemeConfig`.

Configure:
- `colorScheme`: Material 3 light/dark color tokens.
- `typography`: Material 3 text styles used by screens and shared components.
- `shapes`: Material 3 shape tokens.
- `spacing`: app spacing tokens for screen padding, rows, and gaps.
- `decoration`: minimum touch target, loading indicator size/stroke, card elevation, and border width.

Feature screens should read `AppTheme.colors`, `AppTheme.typography`, `AppTheme.spacing`, and `AppTheme.decoration` rather than hardcoding repeated colors or dimensions. `MainActivity` selects `AppThemeDefaults.light()` or `AppThemeDefaults.dark()` and passes the chosen config to `AndroidBaseTheme`.

## Localization

English is the primary locale. Every module that owns user-facing text keeps matching keys in:

```text
src/main/res/values/strings.xml
src/main/res/values-vi/strings.xml
```

ViewModels expose messages as `UiText.Resource`; Composables resolve them with `stringResource` or `UiText.asString`. Domain errors remain typed and are translated at the feature/UI edge.

## Test Coverage

Kover is configured at the root with an 80% line coverage gate for app logic that is practical to test on the JVM: domain models/use cases, error mapping, `UiText`, ViewModels, UiState, and one-off effects. Compose rendering can be covered with UI tests when screen semantics need direct verification.

## Agent Guidance

Read `AGENTS.md`, `CONTEXT.md`, and `.agents/INDEX.md` before changing architecture, dependencies, generated code, or feature boundaries.

Each `.agents/skills/*/SKILL.md` file is intentionally self-contained: open the skill that matches the task, follow its checklist, and run its verification commands before handing work back.
