@echo off

setlocal enabledelayedexpansion

set DEFAULT_GOALS=clean install

set commandArgs=%DEFAULT_GOALS%

if not "%*" == "" (
    set commandArgs=%*
)

echo [INFO] Start a build.

echo [DEBUG] Command arguments : "%commandArgs%"

for /f %%i in (mvn-build-all-targets.txt) do (
    set pomFile=%%i\pom.xml
    if exist !pomFile! (
        call mvn -U -f !pomFile! %commandArgs%
        if not !ERRORLEVEL! == 0 (
            echo [ERROR] Failed a build.
            exit /B !ERRORLEVEL!
        )
    )
)

echo [INFO] Finish a build.
