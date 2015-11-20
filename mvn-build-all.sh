#!/usr/bin/env bash

ARTIFACT_ID_PREFIX="terasoluna-gfw-"
TARGETS_JAVA7_PLUS="parent common jodatime web security-core security-web jpa mybatis3 mybatis2 recommended-dependencies recommended-web-dependencies"
TARGETS_JAVA8_PLUS="validator"
DEFAULT_GOALS="clean install"
ORIGINAL_JAVA_HOME=${JAVA_HOME}

# e.g.)
# 1.7.0_xx : 7
# 1.8.0_xx : 8
declare -i CURRENT_JDK_VERSION=`javac -version 2>&1 | sed 's/javac \([0-9]\).\([0-9]\).*/\2/;'`

commandArgs=${DEFAULT_GOALS}
if test $# -ne 0 ; then
    commandArgs=$@
fi

echo "[INFO] Start a build."

echo "[DEBUG] Command arguments : \"${commandArgs}\""

# Build modules for JDK requirement is 7+
for target in ${TARGETS_JAVA7_PLUS}; do
    artifactId=${ARTIFACT_ID_PREFIX}${target}
    if test -f ${artifactId}/pom.xml ; then
        # Build a target module
        mvn -U -f ${artifactId}/pom.xml ${commandArgs}
        buildResult=$?
        if test ${buildResult} -ne 0 ; then
            echo "[ERROR] Failed a build."
            exit ${buildResult}
        fi
    fi
done

# Build modules for JDK requirement is 8+
for target in ${TARGETS_JAVA8_PLUS}; do
    if test -f ${artifactId}/pom.xml ; then
        artifactId=${ARTIFACT_ID_PREFIX}${target}
        # If present the 'JAVA8_PLUS_HOME' environment variable, install latest module using JDK 8+.
        if test ${CURRENT_JDK_VERSION} -lt 8 && test -n ${JAVA8_PLUS_HOME} ; then
            export JAVA_HOME=${JAVA8_PLUS_HOME}
            mvn -U -f ${artifactId}/pom.xml clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dsource.skip=true
            buildResult=$?
            if test ${buildResult} -ne 0 ; then
                echo "[ERROR] Failed a build on pre install."
                exit ${buildResult}
            fi
        fi
        # Build a target module
        export JAVA_HOME=${ORIGINAL_JAVA_HOME}
        mvn -U -f ${artifactId}/pom.xml ${commandArgs}
        buildResult=$?
        if test ${buildResult} -ne 0 ; then
            echo "[ERROR] Failed a build."
            exit ${buildResult}
        fi
    fi
done

echo "[INFO] Finish a build."

# Display warning logs(less than Java 8)
if test ${CURRENT_JDK_VERSION} -lt 8 ; then
    echo "[WARNING] In below modules, skip a compile(and install, deploy, etc...) because it need the JDK 8+ for full build.";
    for target in ${TARGETS_JAVA8_PLUS}; do
        echo "${ARTIFACT_ID_PREFIX}${target}"
    done
    if test -z ${JAVA8_PLUS_HOME} ; then
        echo "[WARNING] Performed tests using artifact which installed into maven repository. If you want to perform tests using latest module, please define the 'JAVA8_PLUS_HOME' environment variable.";
    fi
fi
