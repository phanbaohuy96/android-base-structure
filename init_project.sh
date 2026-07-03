#!/usr/bin/env bash
set -euo pipefail

NEW_PACKAGE="${1:-}"
NEW_NAME="${2:-}"

if [[ -z "$NEW_PACKAGE" || -z "$NEW_NAME" ]]; then
  echo "Usage: ./init_project.sh com.example.app \"Example App\""
  exit 1
fi

OLD_PACKAGE="com.pbh.androidbase"
OLD_PATH="com/pbh/androidbase"
NEW_PATH="${NEW_PACKAGE//./\/}"

find . -type f \
  ! -path "./.gradle/*" \
  ! -path "./build/*" \
  ! -path "*/build/*" \
  -print0 |
while IFS= read -r -d '' file; do
  case "$file" in
    *.kt|*.kts|*.xml|*.md|*.toml|*.yaml|*.yml|*.json|*.pro|*.properties)
      perl -0pi -e "s/\Q$OLD_PACKAGE\E/$NEW_PACKAGE/g; s/Android Base/$NEW_NAME/g" "$file"
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

echo "Initialized $NEW_NAME at package $NEW_PACKAGE"
