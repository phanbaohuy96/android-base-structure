#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DEST="$ROOT/.agents/skills"
OVERLAY_DIR="$ROOT/.agents/skill-overlays"
INSTALLER="${ANDROID_SKILL_INSTALLER:-${CODEX_HOME:-$HOME/.codex}/skills/.system/skill-installer/scripts/install-skill-from-github.py}"
REPO="${ANDROID_SKILLS_REPO:-android/skills}"
REF="${ANDROID_SKILLS_REF:-main}"

if [[ ! -f "$INSTALLER" ]]; then
  echo "Android skill installer not found: $INSTALLER" >&2
  echo "Set ANDROID_SKILL_INSTALLER to the install-skill-from-github.py path." >&2
  exit 1
fi

mkdir -p "$DEST"

PATHS=(
  build/agp/agp-9-upgrade
  camera/camera1-to-camerax
  device-ai/appfunctions
  devtools/android-cli
  identity/verified-email
  jetpack-compose/adaptive
  jetpack-compose/migration/migrate-xml-views-to-jetpack-compose
  performance/r8-analyzer
  play/engage-sdk-integration
  play/play-billing-library-version-upgrade
  profilers/perfetto-sql
  profilers/perfetto-trace-analysis
  security/android-intent-security
  system/edge-to-edge
  wear/wear-compose-m3
  xr/display-glasses-with-jetpack-compose-glimmer
)

# These upstream skills overlap with project-specific and-* skills.
# Keep their merged guidance in the local skills instead of reinstalling them:
# - testing/testing-setup -> and-testing
# - navigation/navigation-3 -> and-navigation
# - jetpack-compose/theming/styles -> and-theme-usage

TMP="$(mktemp -d "${TMPDIR:-/tmp}/android-skills.XXXXXX")"
cleanup() {
  rm -rf "$TMP"
}
trap cleanup EXIT

apply_overlay() {
  local skill="$1"
  local overlay="$OVERLAY_DIR/$skill/SKILL.after-ime.md"

  if [[ ! -f "$overlay" ]]; then
    return 0
  fi

  python3 - "$DEST/$skill/SKILL.md" "$overlay" <<'PY'
from pathlib import Path
import sys

skill_path = Path(sys.argv[1])
overlay_path = Path(sys.argv[2])
anchor = "### IMEs with Scaffolds code patterns"

text = skill_path.read_text()
overlay = overlay_path.read_text().strip() + "\n\n"

if overlay in text:
    raise SystemExit(0)
if anchor not in text:
    raise SystemExit(f"Overlay anchor not found in {skill_path}: {anchor}")

skill_path.write_text(text.replace(anchor, overlay + anchor, 1))
PY
}

echo "Downloading $REPO@$REF into a temporary directory..."
python3 "$INSTALLER" \
  --repo "$REPO" \
  --ref "$REF" \
  --dest "$TMP" \
  --path "${PATHS[@]}"

LICENSE_FILE="$TMP/LICENSE.txt"
python3 - "$REPO" "$REF" "$LICENSE_FILE" <<'PY'
from pathlib import Path
import sys
import urllib.request

repo, ref, dest = sys.argv[1:]
url = f"https://raw.githubusercontent.com/{repo}/{ref}/LICENSE.txt"
try:
    content = urllib.request.urlopen(url, timeout=30).read()
except Exception as exc:
    raise SystemExit(f"Failed to download {url}: {exc}")
Path(dest).write_bytes(content)
PY

echo "Replacing kept upstream Android skills in $DEST..."
for path in "${PATHS[@]}"; do
  skill="$(basename "$path")"
  if [[ ! -f "$TMP/$skill/SKILL.md" ]]; then
    echo "Downloaded skill is missing SKILL.md: $skill" >&2
    exit 1
  fi

  rm -rf "$DEST/$skill"
  cp -R "$TMP/$skill" "$DEST/$skill"
  cp "$LICENSE_FILE" "$DEST/$skill/LICENSE.txt"
  apply_overlay "$skill"
done

echo "Updated ${#PATHS[@]} upstream Android skills from $REPO@$REF."
echo "Skipped merged overlaps: testing-setup, navigation-3, styles."
echo "Applied local overlays from $OVERLAY_DIR when present."
