# Call chain simplifier
Решение тестового задания для проекта ВКР "Интеграция разработки на R и С++".
# Требуемое ПО
JDK 8 (Oracle JDK, OpenJDK, другой совместимый).
# Сборка
* Загрузите код проекта (git clone, download ZIP, etc.)
* В папке проекта выполните
  * Для Windows:
    ```
    gradlew.bat build
    ```
  * Для Unix-like:
    ```
    chmod +x gradlew
    ./gradlew build
    ```
# Тестирование
* Для Windows:
  ```
  gradlew.bat test
  ```
* Для Unix-like:
  ```
  ./gradlew test
  ```
# Запуск
* Перейдите в директорию build/libs
* Выполните
  ```
  java -jar call-chain-simplifier-1.0-SNAPSHOT.jar
  ```
  или
  ```
  java -jar call-chain-simplifier-1.0-SNAPSHOT.jar -h
  ```
  для показа справки по использованию.
# Особенности
Код в src/main/java/ru/nsu/fit/jbr/simplifier/antlr автоматически сгенерировал с помощью ANTRL4.
