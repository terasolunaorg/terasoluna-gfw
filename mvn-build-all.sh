#!/usr/bin/env bash
#this script is left to keep backward compatibility.

DEFAULT_GOALS="clean install"

commandArgs=${DEFAULT_GOALS}
if test $# -ne 0 ; then
    commandArgs=$@
fi

echo "[INFO] Start a build."

echo "[DEBUG] Command arguments : \"${commandArgs}\""

mvn -U ${commandArgs}
buildResult=$?
if test ${buildResult} -ne 0 ; then
    echo "[ERROR] Failed a build."
    exit ${buildResult}
fi

echo "[INFO] Finish a build."
