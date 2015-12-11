#!/usr/bin/env bash

ARTIFACT_ID_PREFIX="terasoluna-gfw-"
TARGETS="parent common codepoints jodatime web security-core security-web string validator jpa mybatis3 mybatis2 recommended-dependencies recommended-web-dependencies"
TARGETS="${TARGETS} codepoints/catalog/terasoluna-gfw-codepoints-jisx0201 codepoints/catalog/terasoluna-gfw-codepoints-jisx0208 codepoints/catalog/terasoluna-gfw-codepoints-jisx0208kanji codepoints/catalog/terasoluna-gfw-codepoints-jisx0213kanji"
DEFAULT_GOALS="clean install"

commandArgs=${DEFAULT_GOALS}
if test $# -ne 0 ; then
    commandArgs=$@
fi

echo "[INFO] Start a build."

echo "[DEBUG] Command arguments : \"${commandArgs}\""

for target in ${TARGETS}; do
    artifactId=${ARTIFACT_ID_PREFIX}${target}
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
