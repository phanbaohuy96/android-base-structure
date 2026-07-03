#!/usr/bin/env python3
import json
import sys
from pathlib import Path

payload = json.load(sys.stdin)
tool_input = payload.get("tool_input", {})
path = tool_input.get("file_path") or tool_input.get("path") or ""
target = Path(path)
parts = set(target.parts)
name = target.name
suffix = target.suffix

deny_names = {"google-services.json", "local.properties", "keystore.properties"}
deny_suffixes = {".jks", ".keystore"}
deny_prefixes = (".env",)
ask_names = {"settings.gradle.kts", "AndroidManifest.xml"}
ask_suffixes = {".gradle", ".kts", ".toml", ".yml", ".yaml"}

if "build" in parts or "generated" in parts or name in deny_names or suffix in deny_suffixes or name.startswith(deny_prefixes):
    print(json.dumps({"decision": "deny", "reason": f"Do not edit generated or sensitive file: {path}"}))
    sys.exit(0)

if name in ask_names or suffix in ask_suffixes or ".github" in parts:
    print(json.dumps({"decision": "ask", "reason": f"High-impact project file: {path}"}))
    sys.exit(0)

print(json.dumps({"decision": "allow"}))
