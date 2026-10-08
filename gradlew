#!/bin/sh
#
# Gradle wrapper script.
# Запуск: ./gradlew build | ./gradlew test | ./gradlew run
#
APP_BASE_NAME=${0##*/}
APP_HOME=$(cd "${0%/*}" >/dev/null 2>&1 && pwd -P)

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if command -v java >/dev/null 2>&1; then
    JAVACMD=java
elif [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    echo "ERROR: JAVA_HOME is not set and no 'java' command found." >&2
    exit 1
fi

exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
