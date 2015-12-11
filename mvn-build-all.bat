@echo off

setlocal enabledelayedexpansion

set ARTIFACT_ID_PREFIX=terasoluna-gfw-
set TARGETS=parent common codepoints jodatime web security-core security-web string validator jpa mybatis3 mybatis2 recommended-dependencies recommended-web-dependencies
set TARGETS="${TARGETS} codepoints/catalog/terasoluna-gfw-codepoints-jisx0201 codepoints/catalog/terasoluna-gfw-codepoints-jisx0208 codepoints/catalog/terasoluna-gfw-codepoints-jisx0208kanji codepoints/catalog/terasoluna-gfw-codepoints-jisx0213kanji"
set DEFAULT_GOALS=clean install

set commandArgs=%DEFAULT_GOALS%

if not "%*" == "" (
    set commandArgs=%*
)

echo [INFO] Start a build.

echo [DEBUG] Command arguments : "%commandArgs%"

for %%i in (%TARGETS%) do (
    set pomFile=%ARTIFACT_ID_PREFIX%%%i\pom.xml
    if exist !pomFile! (
        call mvn -U -f !pomFile! %commandArgs%
        if not !ERRORLEVEL! == 0 (
            echo [ERROR] Failed a build.
            exit /B !ERRORLEVEL!
        )
    )
)

echo [INFO] Finish a build.
