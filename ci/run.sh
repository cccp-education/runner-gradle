#!/usr/bin/env bash
# GBL-013 US-5 (wave 2) — CI contract for runner-gradle.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

job="${1:-}"
case "$job" in
  fast|test)
    ./gradlew build -PsiteName="${SITE_NAME:-ci}" --no-daemon --console=plain
    ;;
  *)
    printf 'unsupported job for runner-gradle: %s\n' "${job:-<none>}" >&2
    printf 'supported: fast | test\n' >&2
    exit 2
    ;;
esac
