#!/usr/bin/env bash

TARGETS=`cat mvn-build-all-targets.txt`
DEFAULT_GOALS="clean install"

commandArgs=${DEFAULT_GOALS}
if test $# -ne 0 ; then
    commandArgs=$@
fi

echo "[INFO] Start a build."

echo "[DEBUG] Command arguments : \"${DEFAULT_GOALS}\""

mvn -U -f terasoluna-gfw-parent/pom.xml ${DEFAULT_GOALS}
buildResult=$?
if test ${buildResult} -ne 0 ; then
    echo "[ERROR] Failed a build."
    exit ${buildResult}
fi

echo "[DEBUG] Command arguments : \"${commandArgs}\""

mvn -U ${commandArgs}
buildResult=$?
if test ${buildResult} -ne 0 ; then
    echo "[ERROR] Failed a build."
    exit ${buildResult}
fi

echo "[DEBUG] Command arguments : \"${DEFAULT_GOALS}\""

mvn -U -f terasoluna-gfw-dependencies/pom.xml ${DEFAULT_GOALS}
buildResult=$?
if test ${buildResult} -ne 0 ; then
    echo "[ERROR] Failed a build."
    exit ${buildResult}
fi

echo "[INFO] Finish a build."
