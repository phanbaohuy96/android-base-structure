## Summary

## Verification

- [ ] `./gradlew :app:assembleDevDebug`
- [ ] `./gradlew testDevDebugUnitTest`
- [ ] `./gradlew detekt spotlessCheck`

## Checklist

- [ ] Module boundaries preserved.
- [ ] Feature modules do not import `:data`.
- [ ] No generated files edited.
- [ ] No secrets, signing files, `local.properties`, Firebase config, or `.env*` committed.
- [ ] KSP/Hilt/Room output regenerated through Gradle when annotations changed.
- [ ] User-facing text is localized or intentionally prototype-only.

## Caution Areas

- [ ] Signing configuration changed.
- [ ] Product flavors changed.
- [ ] Network security config changed.

## Screenshots
