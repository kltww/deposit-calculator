# Deposit Calculator — Лабораторная работа № 2

Инструменты и средства разработки сценариев в системах непрерывной интеграции.
Java 17, JUnit 5.14.4, Apache Ant (junitlauncher), Apache Maven 3.9.x, Gradle Wrapper, GitHub Actions.

Простая бизнес-логика без сложных банковских формул:
- простые проценты: `P * (1 + r*y/100)`
- сложные проценты (капитализация): `P * (1 + r/100)^y`

## Структура проекта

```
deposit-calculator/
├── src/main/java/by/student/deposit/
│   ├── model/Deposit.java
│   ├── service/DepositCalculator.java
│   └── Main.java
├── src/test/java/by/student/deposit/
│   └── DepositCalculatorTest.java   # 9 тестов
├── lib/                             # JAR для Ant (JUnit 5, уже скачаны)
├── build.xml                        # Ant: clean, init, compile, compile-tests, test, report, jar, run, all
├── pom.xml                          # Maven: compiler 3.14.1, surefire 3.6.0, jar 3.5.1
├── build.gradle                     # расширение
├── settings.gradle                  # расширение
├── gradlew / gradlew.bat + gradle/wrapper/
└── .github/workflows/maven.yml      # CI: mvn -B clean verify
```

## Требования

- JDK 17 (`java -version`, `javac -version`)
- Ant 1.10.x (`ant -version`) — для `junitlauncher` нужен `ant-junitlauncher.jar` в `ANT_HOME/lib`
- Maven 3.9.x (`mvn -version`)
- Для Gradle глобальная установка не нужна — используется Wrapper 8.10.2

## Часть 1. Проверка окружения

```powershell
java -version
javac -version
ant -version
mvn -version
```

## Часть 2. Apache Ant (процедурная модель)

`build.xml` явно описывает свойства, classpath (`lib/*.jar`) и target'ы.
JUnit 5 запускается **только** через `<junitlauncher>` (тег `<junit>` — для JUnit 3/4).
`listener type="legacy-xml"` дает XML для `junitreport`, `legacy-plain` — текст в консоль.

```powershell
ant -p            # список targets
ant clean         # удалить build/
ant compile       # build/classes (init без скрытого clean!)
ant test          # компиляция тестов + JUnit 5; при failure — BUILD FAILED
ant report        # build/reports/xml + build/reports/html
ant jar           # build/jar/DepositCalculator.jar (Main-Class)
ant run           # запуск JAR, печать отчета по вкладу
ant all           # clean → report → jar (полный цикл)
ant clean all     # гарантированно чистая полная сборка
```

Важно: `compile` зависит от `init`, `init` НЕ зависит от `clean`.
Поэтому `ant compile` не уничтожает прошлую сборку. Чистый цикл — только `ant all` / `ant clean all`.

Если `Problem: failed to create task or type junitlauncher`:
скопировать `ant-junitlauncher.jar` из дистрибутива Ant в `ANT_HOME/lib`.

Если `NoClassDefFoundError` в `ant test`:
проверить `lib/`: junit-jupiter-api, junit-jupiter-engine, junit-jupiter-params,
junit-platform-commons, junit-platform-engine, junit-platform-launcher, opentest4j, apiguardian.

## Часть 3. Apache Maven (декларативная модель)

Стандартная структура `src/main/java`, `src/test/java` — Convention over Configuration,
описывать ее в POM не нужно. `dependency` — библиотека, `plugin` — расширение lifecycle.

```powershell
mvn clean             # удалить target/
mvn compile           # target/classes
mvn test              # Surefire → target/surefire-reports/*.xml, *.txt
mvn package           # тесты + JAR target/deposit-calculator-1.0.0-SNAPSHOT.jar
mvn clean package     # clean + все фазы до package
mvn clean verify      # полный учебный цикл
mvn surefire-report:report   # HTML: target/reports/surefire.html (в старых версиях: target/site/surefire-report.html)
mvn dependency:tree
mvn help:effective-pom
```

Минимальная последовательность для HTML-отчета:
```powershell
mvn test
mvn surefire-report:report
# открыть target/reports/surefire.html
# (в старых версиях плагина — target/site/surefire-report.html)
```

XML нужен CI (машинно-читаемый passed/failed/skipped), HTML — человеку.

## Эксперимент № 1 — намеренно падающий тест

1. `mvn clean verify` → BUILD SUCCESS.
2. В `DepositCalculatorTest.simpleInterest` заменить `1300.0` на `1400.0`.
3. `mvn test` → `Tests run: 9, Failures: 1` → BUILD FAILURE.
4. Вернуть `1300.0`, `mvn clean verify` → BUILD SUCCESS.

Цепочка: изменение кода → unit test failed → Maven выходит с ненулевым статусом →
CI помечает stage как failed → JAR не публикуется. Именно так тест блокирует релиз.

## Сравнение Ant / Maven

| Критерий | Apache Ant | Apache Maven |
|---|---|---|
| Основной файл | build.xml | pom.xml |
| Модель | Процедурная / императивная | Декларативная (фазы lifecycle) |
| Жизненный цикл | Нет, граф target'ов вручную | Фиксированный: validate → compile → test-compile → test → package → verify → install |
| Зависимости | Вручную через lib/ + classpath | Автоматически через репозитории (Dependency Management) |
| Структура папок | Произвольная | Стандартизированная (Convention over Configuration) |

## Часть 4. Расширение: Gradle + CI

```powershell
gradlew.bat build   # Windows
gradlew.bat test
gradlew.bat run
./gradlew build     # Linux/macOS (chmod +x gradlew)
# HTML-отчет: build/reports/tests/test/index.html
```

CI (`.github/workflows/maven.yml`): по `push` / `pull_request` в `main`
на `ubuntu-latest` ставит JDK 17 (Temurin, кэш maven), выполняет `mvn -B clean verify`
и всегда (`if: always()`) загружает `target/surefire-reports/` как artifact.
CI не содержит бизнес-логики — он запускает тот же Maven, что и разработчик локально.

## Ожидаемые артефакты

После Ant: `build/classes/`, `build/test-classes/`, `build/reports/xml/`, `build/reports/html/`, `build/jar/DepositCalculator.jar`.
После Maven: `target/classes/`, `target/test-classes/`, `target/surefire-reports/`, `target/*.jar`, после отчета — `target/site/surefire-report.html`.
