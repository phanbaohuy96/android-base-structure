# Context Glossary

## Feature module

An Android library module containing screens, ViewModels, feature routes, and feature-local UI state.

_Avoid_: package, page folder, scene.

## UseCase

A domain operation with one public `operator fun invoke`.

_Avoid_: manager, service, helper.

## UiState

A sealed interface describing renderable screen state.

_Avoid_: view data, screen model, status bag.

## Effect

A one-off UI event emitted from a ViewModel through a `Channel`/`Flow`.

_Avoid_: event bus, callback, action stream.

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
