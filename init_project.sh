#!/usr/bin/env bash
set -euo pipefail

NEW_PACKAGE="${1:-}"
NEW_NAME="${2:-}"

if [[ -z "$NEW_PACKAGE" || -z "$NEW_NAME" ]]; then
  echo "Usage: ./init_project.sh com.example.app \"Example App\""
  exit 1
fi

if ! printf '%s' "$NEW_PACKAGE" | grep -Eq '^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$'; then
  echo "Package must be lowercase, dot-separated, at least two segments: com.example.app" >&2
  exit 1
fi

OLD_PACKAGE="com.pbh.androidbase"
OLD_PATH="com/pbh/androidbase"

# Docs and agent skills spell the package as a path (`com/pbh/androidbase`) as often as they
# spell it with dots, so both forms have to be substituted.
# `tr` rather than ${NEW_PACKAGE//./\/}: bash 3.2, which is what macOS ships, keeps the
# backslash in the replacement and produces a single directory literally named
# `com\/example\/app`. Newer bash strips it, so the bug is invisible on Linux CI.
NEW_PATH=$(printf '%s' "$NEW_PACKAGE" | tr '.' '/')

# "Android Base" is only one of the three spellings the template uses for itself.
# `AndroidBase` appears glued into identifiers (`AndroidBaseTheme`, `@style/Theme.AndroidBase`)
# and `android-base-structure` is the Gradle root project name; neither contains a space, so
# neither is reached by substituting the display name alone.
NEW_PASCAL=$(printf '%s' "$NEW_NAME" | sed -E 's/[^A-Za-z0-9]+/ /g' |
  awk '{for (i = 1; i <= NF; i++) printf "%s%s", toupper(substr($i, 1, 1)), substr($i, 2)}')
NEW_KEBAB=$(printf '%s' "$NEW_NAME" | sed -E 's/[^A-Za-z0-9]+/-/g' |
  tr '[:upper:]' '[:lower:]' | sed -E 's/^-+//; s/-+$//')

if [[ -z "$NEW_PASCAL" || -z "$NEW_KEBAB" ]]; then
  echo "Name must contain at least one letter or digit: \"$NEW_NAME\"" >&2
  exit 1
fi

find . -type f \
  ! -path "./init_project.sh" \
  ! -path "./.git/*" \
  ! -path "./.gradle/*" \
  ! -path "./build/*" \
  ! -path "*/build/*" \
  -print0 |
while IFS= read -r -d '' file; do
  case "$file" in
    *.kt|*.kts|*.xml|*.md|*.toml|*.yaml|*.yml|*.json|*.pro|*.properties|*.py|*.sh|*.txt)
      # Values travel through the environment, so a name containing / $ @ or \ cannot
      # break out of the substitution.
      OLD_PACKAGE="$OLD_PACKAGE" \
      OLD_PATH="$OLD_PATH" \
      NEW_PACKAGE="$NEW_PACKAGE" \
      NEW_PATH="$NEW_PATH" \
      NEW_NAME="$NEW_NAME" \
      NEW_PASCAL="$NEW_PASCAL" \
      NEW_KEBAB="$NEW_KEBAB" \
      perl -0pi -e '
        s/\Q$ENV{OLD_PACKAGE}\E/$ENV{NEW_PACKAGE}/g;
        s/\Q$ENV{OLD_PATH}\E/$ENV{NEW_PATH}/g;
        s/\Qandroid-base-structure\E/$ENV{NEW_KEBAB}/g;
        s/\QAndroid Base\E/$ENV{NEW_NAME}/g;
        s/\QAndroidBase\E/$ENV{NEW_PASCAL}/g;
      ' "$file"
      ;;
  esac
done

find app core data domain -type d -path "*/$OLD_PATH" -print0 |
while IFS= read -r -d '' dir; do
  target="${dir%$OLD_PATH}$NEW_PATH"
  mkdir -p "$target"
  find "$dir" -mindepth 1 -maxdepth 1 -exec mv {} "$target"/ \;
done

find app core data domain -type d -empty -delete

# The rename is not finished until nothing answers to the old name. Silent partial renames are
# how `AndroidBaseTheme` survived into a downstream project.
leftovers=$(grep -rIlF -e "$OLD_PACKAGE" -e "$OLD_PATH" -e 'AndroidBase' -e 'android-base-structure' . \
  --exclude-dir=.git --exclude-dir=.gradle --exclude-dir=build --exclude=init_project.sh 2>/dev/null || true)
if [[ -n "$leftovers" ]]; then
  echo "Rename incomplete — the old name still appears in:" >&2
  printf '  %s\n' $leftovers >&2
  exit 1
fi

echo "Initialized $NEW_NAME at package $NEW_PACKAGE"
echo "  identifiers: ${NEW_PASCAL}Theme, @style/Theme.$NEW_PASCAL"
echo "  gradle root: $NEW_KEBAB"

# Renaming the package changes where its imports sort alphabetically, so a freshly initialized
# project fails its own ktlint gate until they are re-sorted. Doing it here is the difference
# between `./gradlew check` being green on the first run and being red for a reason the user did
# not cause. It needs the Android SDK, so a failure here is reported rather than fatal.
if ./gradlew --quiet spotlessApply >/dev/null 2>&1; then
  echo "  formatting:  imports re-sorted (spotlessApply)"
else
  echo "  formatting:  run ./gradlew spotlessApply once the Android SDK is configured" >&2
fi
