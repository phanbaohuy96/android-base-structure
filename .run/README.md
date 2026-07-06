# Run Configurations

Android Studio auto-discovers the Gradle run configurations in this directory after
opening the project:

- `Unit Tests (dev)` runs `testDevDebugUnitTest`.
- `Lint (detekt + spotless)` runs `detekt spotlessCheck`.
- `Coverage (kover)` runs the dev-debug/module Kover verification tasks.

The Android App launch configuration is intentionally not checked in because its
`<module>` value depends on the local Android Studio import model. Generate it once
from Android Studio for the `devDebug` variant when you need one-click deployment.
