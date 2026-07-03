#!/usr/bin/env bash
set -euo pipefail

if [[ ! -x ./node_modules/.bin/context7-mcp ]]; then
  echo "context7 MCP is not installed. Run npm install first." >&2
  exit 1
fi

exec ./node_modules/.bin/context7-mcp
