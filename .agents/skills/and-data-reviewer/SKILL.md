---
name: and-data-reviewer
description: Review data-layer and clean-architecture boundaries.
---

## When to use

Use during review of repository, API, Room, DataStore, mapper, Hilt binding, or clean-architecture boundary changes.

## Kotlin patterns/refs

Trace the path: UI -> ViewModel -> UseCase -> Repository port -> data implementation -> remote/local source -> mapper. Each boundary should translate outward-facing details into the next inner layer's language.

## Checklist

- Feature modules do not import `:data` types.
- Domain stays pure Kotlin/JVM with no Android imports.
- DTOs, Room entities, and DataStore keys do not leak outside `:data`/`:core` infrastructure.
- Repositories return `AppResult`/domain models, not Retrofit/Room primitives.
- Offline-cache behavior is deterministic and tested.
- Mock/real source selection is flavor/config driven, not hardcoded in features.
- Hilt modules are installed in the correct component.

## Common mistakes

- Accepting a repository that catches everything as `Unknown`.
- Letting a DAO entity become the domain model because fields match.
- Putting repository implementation bindings in feature modules.
- Reviewing only compile success and not the dependency graph.

## Verification

- `rtk .agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`
- `rtk ./gradlew :data:compileDevDebugKotlin :domain:test`
- `rtk rg -n "com\\.pbh\\.androidbase\\.data" feature-auth/src feature-home/src`

## Related

`and-data-layer`, `and-dependency-injection`, `and-error-handling`.
