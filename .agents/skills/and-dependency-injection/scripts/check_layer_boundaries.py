#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[4]
FAILURES = []

def scan(pattern):
    return [p for p in ROOT.glob(pattern) if p.is_file()]

for path in scan("domain/src/**/*.kt"):
    text = path.read_text()
    if "import android." in text or "import androidx." in text:
        FAILURES.append(f"{path}: domain must not import Android APIs")

# Features now live inside :app (package com.pbh.androidbase.feature.*), so the old module-level
# "feature must not depend on :data" edge no longer exists. Enforce it as a package rule instead:
# feature packages must reach data only through :domain use cases, never import :data directly.
for path in scan("app/src/**/*.kt"):
    if "/com/pbh/androidbase/feature/" not in path.as_posix():
        continue
    text = path.read_text()
    if "import com.pbh.androidbase.data." in text:
        FAILURES.append(f"{path}: feature packages must not import :data (go through :domain use cases)")

for path in scan("**/src/**/*.kt"):
    text = path.read_text()
    if "@Inject\n    lateinit var" in text or "@Inject lateinit var" in text:
        if "Activity" not in text and "Fragment" not in text and "Application" not in text:
            FAILURES.append(f"{path}: avoid field injection outside composition entrypoints")

if FAILURES:
    print("\n".join(FAILURES))
    sys.exit(1)

print("Layer boundary checks passed.")
