#!/usr/bin/env python3
import json
import subprocess
import sys
from pathlib import Path

payload = json.load(sys.stdin)
tool_input = payload.get("tool_input", {})
path = tool_input.get("file_path") or tool_input.get("path") or ""
target = Path(path)

if target.suffix == ".kt" and "build" not in target.parts and "generated" not in target.parts:
    subprocess.run(["./gradlew", "spotlessApply"], check=False)

print(json.dumps({"ok": True}))
