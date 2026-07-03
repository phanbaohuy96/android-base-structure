# Troubleshooting

## Hilt Cannot Find a Binding

Check that interfaces are bound in a module installed in `SingletonComponent`, and that the implementation constructor is injectable or provided.

## Room Schema or KSP Fails

Do not edit generated files. Fix annotations or DAO signatures, then rerun the Gradle task.

## Compose Type-safe Route Fails

Ensure the route class is `@Serializable` and that the module applies the Kotlin serialization plugin.

## Feature Imports Data

Move the dependency behind a domain repository port and bind the implementation in `:data`.
