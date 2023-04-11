# Требования к системе
* Docker должен быть установлен и настроен (
[Docker Desktop for Linux](https://docs.docker.com/desktop/install/linux-install/),
[Install Docker Engine on Ubuntu](https://docs.docker.com/engine/install/ubuntu/))
* Java 17 должна быть установлена ([Install Java 17](https://www.itzgeek.com/how-tos/linux/ubuntu-how-tos/install-java-jdk-17-on-ubuntu-22-04.html))

# Инструкция по сборке
1. Собрать проект с помощью запуска команды `./gradlew clean build
   ` находясь в `*/discount` директории
2. В терминале запустите проект с помощью команды `docker-compose up -d`
3. Запустив команду `docker ps --filter ancestor=discount-service
   ` можно получить информацию о всех запущенных репликах сервиса