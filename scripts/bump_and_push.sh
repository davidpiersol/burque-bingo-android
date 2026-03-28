#!/usr/bin/env bash
# Run after a successful build/sim when you want a new Play-visible version on GitHub.
# - Increments versionCode by 1
# - Increments patch in versionName (1.2.3 -> 1.2.4; falls back to 1.0.N if unparsable)
# - Commits app/build.gradle.kts
# - Pushes current branch to origin when `git remote get-url origin` works
#
# Usage (from repo root):
#   ./scripts/bump_and_push.sh
#   ./scripts/bump_and_push.sh "Optional commit message suffix"

set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
FILE="$ROOT/app/build.gradle.kts"

vc_line=$(grep -E '^\s*versionCode\s*=' "$FILE" | head -1)
vn_line=$(grep -E '^\s*versionName\s*=' "$FILE" | head -1)
vc=$(echo "$vc_line" | sed -E 's/.*versionCode = //;s/[[:space:]]//g')
vn=$(echo "$vn_line" | sed -E 's/.*versionName = "//;s/".*//')

new_vc=$((vc + 1))
IFS='.' read -r p1 p2 p3 <<< "${vn}"
if [[ -n "${p1:-}" && -n "${p2:-}" && -n "${p3:-}" ]] && [[ "$p1$p2$p3" =~ ^[0-9]+$ ]]; then
  new_vn="${p1}.${p2}.$((p3 + 1))"
else
  new_vn="1.0.${new_vc}"
fi

if [[ "$(uname)" == "Darwin" ]]; then
  sed -i '' -E "s/(versionCode = )${vc}/\\1${new_vc}/" "$FILE"
  sed -i '' -E "s/(versionName = \")[^\"]+/\\1${new_vn}/" "$FILE"
else
  sed -i -E "s/(versionCode = )${vc}/\\1${new_vc}/" "$FILE"
  sed -i -E "s/(versionName = \")[^\"]+/\\1${new_vn}/" "$FILE"
fi

msg="Bump version to ${new_vn} (${new_vc})"
if [[ -n "${1:-}" ]]; then
  msg="${msg} — ${1}"
fi

git add app/build.gradle.kts
git commit -m "$msg"

if git remote get-url origin &>/dev/null; then
  branch=$(git branch --show-current)
  git push -u origin "$branch"
  echo "Pushed ${branch} to origin."
else
  echo "No GitHub remote yet. Add and push once:"
  echo "  git remote add origin https://github.com/ORG/REPO.git"
  echo "  git push -u origin main"
fi

echo "Now at versionName=${new_vn} versionCode=${new_vc}"
