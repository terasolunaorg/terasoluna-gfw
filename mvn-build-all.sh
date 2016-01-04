#!/usr/bin/env bash

TARGETS=`cat mvn-build-all-targets.txt`
DEFAULT_GOALS="clean install"

commandArgs=${DEFAULT_GOALS}
if test $# -ne 0 ; then
    commandArgs=$@
fi

echo "[INFO] Start a build."

echo "[DEBUG] Command arguments : \"${commandArgs}\""

for artifactId in ${TARGETS}; do
    if test -f ${artifactId}/pom.xml ; then
        mvn -U -f ${artifactId}/pom.xml ${commandArgs}
        buildResult=$?
        if test ${buildResult} -ne 0 ; then
            echo "[ERROR] Failed a build."
            exit ${buildResult}
        fi
    fi
done

echo "[INFO] Finish a build."
