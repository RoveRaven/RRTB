FROM iits/jdk17:jdk-17-alpine
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=test_rrtb_bot
ENV BOT_TOKEN=5561589568:AAFBIobS9e0Qu6xBw_KanymKV0AcuqbQY-M
ENV BOT_DB_USERNAME=prod_rrtb_db_user
ENV BOT_DB_PASSWORD=password
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-jar","/app.jar"]