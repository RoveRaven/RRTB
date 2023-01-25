FROM iits/jdk17:jdk-17-alpine
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=test_rrtb_bot
ENV BOT_TOKEN=5561589568:AAFBIobS9e0Qu6xBw_KanymKV0AcuqbQY-M
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}","-jar","/app.jar"]