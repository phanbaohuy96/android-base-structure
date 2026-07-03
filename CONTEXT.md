# Context Glossary

## Feature package

A package under `app/src/main/kotlin/com/pbh/androidbase/feature/**` containing screens, ViewModels, feature routes, and feature-local UI state/effects.

_Avoid_: feature module, page folder, scene.

## BaseViewModel

The shared `:core` ViewModel base that owns immutable `state: StateFlow<S>`, one-off `effects: Flow<E>`, and safe coroutine launching.

_Avoid_: ViewModelBase, bloc base, state holder helper.

## BaseScreen

The shared Compose screen wrapper that collects a `BaseViewModel`, hosts `AppScaffold`, and auto-shows `MessageEffect` snackbars.

_Avoid_: screen base class, route wrapper, scaffold helper.

## UseCase

A domain operation with one public `operator fun invoke`.

_Avoid_: manager, service, helper.

## UiState

A sealed interface describing renderable screen state.

_Avoid_: view data, screen model, status bag.

## Effect

A one-off UI event emitted from a ViewModel through a `Channel`/`Flow`.

_Avoid_: event bus, callback, action stream.

## UiEffect

The marker interface for feature-specific sealed effect hierarchies emitted by `BaseViewModel`.

_Avoid_: raw event, command object.

## MessageEffect

A `UiEffect` carrying a `UiText` message that `BaseScreen` shows in the snackbar automatically.

_Avoid_: snackbar string, toast command.

## ShowMessage

A feature-local effect case implementing `MessageEffect`; keep it inside the feature's sealed effect type.

_Avoid_: shared global snackbar event, raw string message.

## Repository

A domain port that exposes app data operations.

_Avoid_: API client, DAO wrapper.

## RemoteDataSource

The data-layer object that talks to Retrofit APIs or mocks those calls.

_Avoid_: repository, network helper.

## LocalDataSource

The data-layer object that talks to Room, DataStore, or encrypted local stores.

_Avoid_: cache manager.

## Mapper

A small conversion function at the data/domain boundary.

_Avoid_: transformer service.

## Screen

A Compose function that renders UI and delegates behavior to a ViewModel.

_Avoid_: route, page, activity.

## Route

A `@Serializable` navigation destination used by Navigation Compose.

_Avoid_: screen name, path string.

## SessionStore

The data-layer owner of session persistence. Metadata uses Preferences DataStore; tokens use encrypted preferences.

_Avoid_: auth cache, token singleton.
