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

for module in ("feature-auth", "feature-home"):
    for path in scan(f"{module}/src/**/*.kt"):
        text = path.read_text()
        if "com.pbh.androidbase.data" in text:
            FAILURES.append(f"{path}: feature modules must not import :data")

for path in scan("**/src/**/*.kt"):
    text = path.read_text()
    if "@Inject\n    lateinit var" in text or "@Inject lateinit var" in text:
        if "Activity" not in text and "Fragment" not in text and "Application" not in text:
            FAILURES.append(f"{path}: avoid field injection outside composition entrypoints")

if FAILURES:
    print("\n".join(FAILURES))
    sys.exit(1)

print("Layer boundary checks passed.")
