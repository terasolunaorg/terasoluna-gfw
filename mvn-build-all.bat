@echo off

setlocal enabledelayedexpansion

set DEFAULT_GOALS=clean install

set commandArgs=%DEFAULT_GOALS%

if not "%*" == "" (
    set commandArgs=%*
)

echo [INFO] Start a build.

echo [DEBUG] Command arguments : "%DEFAULT_GOALS%"

call mvn -U -f terasoluna-gfw-parent\pom.xml %DEFAULT_GOALS%
        if not !ERRORLEVEL! == 0 (
            echo [ERROR] Failed a build.
            exit /B !ERRORLEVEL!
        )

echo [DEBUG] Command arguments : "%commandArgs%"

call mvn -U %commandArgs%
        if not !ERRORLEVEL! == 0 (
            echo [ERROR] Failed a build.
            exit /B !ERRORLEVEL!
        )

echo [DEBUG] Command arguments : "%DEFAULT_GOALS%"

call mvn -U -f terasoluna-gfw-dependencies\pom.xml %DEFAULT_GOALS%
        if not !ERRORLEVEL! == 0 (
            echo [ERROR] Failed a build.
            exit /B !ERRORLEVEL!
        )

echo [INFO] Finish a build.
