# Agent Domain Notes

Use `CONTEXT.md` for project language before naming new files, classes, states, or modules. Architecture decisions should live in `docs/adr/` when they affect module graph, data flow, persistence, API shape, security, or build tooling.

When reviewing a change, map it to the glossary terms first. If the implementation invents synonyms for established terms, prefer renaming over documenting the synonym.

The current module graph is `:app -> :core/:data/:domain`, `:data -> :core/:domain`, `:core -> :domain`, and `:domain` is Kotlin/JVM only.
Feature code is a package inside `:app`, not a separate Gradle module.
Feature packages talk to use cases and domain models; they never import `com.pbh.androidbase.data.*`.
Shared UI plumbing lives in `:core` as `BaseViewModel`, `BaseScreen`, `UiEffect`, and `MessageEffect`.
Data-layer details such as Retrofit DTOs, Room entities, DataStore keys, and repository implementations stay in `:data`.
