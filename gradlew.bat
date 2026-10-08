@rem Gradle wrapper script for Windows.
@rem Запуск: gradlew.bat build | gradlew.bat test | gradlew.bat run
@echo off
set APP_HOME=%~dp0
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
where java >nul 2>nul
if %ERRORLEVEL%==0 (
    set JAVACMD=java
) else (
    if defined JAVA_HOME (
        set JAVACMD=%JAVA_HOME%\bin\java.exe
    ) else (
        echo ERROR: JAVA_HOME is not set and no 'java' command found.
        exit /b 1
    )
)
"%JAVACMD%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
