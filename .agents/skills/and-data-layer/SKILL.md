---
name: and-data-layer
description: Implement Retrofit, Room, mappers, and repository implementations.
---

## When to use

Use for API services, DTOs, Room entities/DAOs, DataStore, repository implementations, mock/real data-source swaps, or mapper work.

## Kotlin patterns/refs

Repository ports live in `:domain`; implementations live in `:data`. Feature packages depend on use cases and domain models, never data implementations. DTO/entity/domain conversion happens through explicit mapper functions at the data/domain boundary.

Repository methods should return domain types and `AppResult<T>`, or expose `Flow<DomainType>` for a single source of truth. Network and persistence exceptions must be mapped to `DomainError`.

## Checklist

- Keep Retrofit DTOs out of `:domain` and feature packages.
- Keep Room entities out of `:domain` and feature packages.
- Use `NetworkErrorMapper` or a local mapper to convert transport failures.
- Keep cache policy visible in repository code; document fallback behavior in tests.
- Inject dispatchers or data sources when work needs concurrency control.
- Use localized Android resources only for sample/display seed content, never domain errors.
- Add focused tests for mappers, repositories, or use cases when behavior changes.

## Common mistakes

- Mapping DTOs in ViewModels.
- Returning Retrofit `Response` or exceptions from repositories.
- Letting features import `com.pbh.androidbase.data`.
- Silently falling back to stale cache without an effect or state that explains it.

## Verification

- `./gradlew :data:kspDevDebugKotlin`
- `./gradlew :data:compileDevDebugKotlin`
- `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`

## Related

`and-error-handling`, `and-dependency-injection`, `and-testing`.
