---
name: and-dependency-injection
description: Wire Hilt bindings and providers.
---

## When to use

Use for repository bindings, data-source swaps, use case providers, interceptor/client builders, database providers, and app graph wiring.

## Kotlin patterns/refs

Use constructor injection for concrete classes. Use `@Binds` for interface-to-implementation bindings. Use `@Provides` for builders, external SDK types, conditional flavor wiring, and values that require runtime configuration.

App-wide infrastructure belongs in `SingletonComponent`. Android entrypoints (`Application`, `Activity`, Hilt ViewModels) may receive injected dependencies; avoid field injection elsewhere.

## Checklist

- Bind domain repository ports to data implementations in `:data`.
- Keep use case providers in the composition root or a clear DI module.
- Scope singletons intentionally; do not add `@Singleton` to stateful classes by habit.
- Use `@ApplicationContext` with `@param:` on constructor parameters.
- Keep mock/real swaps tied to `BuildConfig` or flavor config.
- Run the layer-boundary script after dependency changes.

## Common mistakes

- Putting app-wide bindings in feature packages.
- Adding a dependency cycle to fix a missing binding.
- Using `@Provides` where constructor injection or `@Binds` is simpler.
- Field-injecting plain Kotlin classes.

## Verification

- `./gradlew :app:kspDevDebugKotlin`
- `./gradlew :app:assembleDevDebug`
- `.agents/skills/and-dependency-injection/scripts/check_layer_boundaries.py`

## Related

`and-module-scaffold`, `and-code-generation`, `and-data-layer`.
