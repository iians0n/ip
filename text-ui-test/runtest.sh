#!/usr/bin/env bash
#
# Compiles GOAT, feeds it input.txt, and compares the output with EXPECTED.TXT.
#
# Usage:
#   ./text-ui-test/runtest.sh            run the test and report PASSED/FAILED
#   ./text-ui-test/runtest.sh --bless    accept the current output as the new expected

set -u

cd "$(dirname "$0")/.." || exit 1

JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home -v 25)}"

rm -rf out
mkdir -p out
if ! "$JAVA_HOME/bin/javac" -d out src/main/java/*.java; then
    echo "BUILD FAILURE"
    exit 1
fi

"$JAVA_HOME/bin/java" -cp out GOAT < text-ui-test/input.txt > text-ui-test/ACTUAL.TXT

if [ "${1:-}" = "--bless" ]; then
    cp text-ui-test/ACTUAL.TXT text-ui-test/EXPECTED.TXT
    echo "EXPECTED.TXT updated from this run. Read the diff above before committing it."
    exit 0
fi

if diff -u text-ui-test/EXPECTED.TXT text-ui-test/ACTUAL.TXT; then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi
