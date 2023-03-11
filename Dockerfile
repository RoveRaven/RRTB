FROM iits/jdk17:jdk-17-alpine
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=your_bot_name
ENV BOT_TOKEN=Your_bot_token
ENV BOT_DB_USERNAME=prod_rrtb_db_user
ENV BOT_DB_PASSWORD=password
ENV ADMINS=admins_list
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dbot.admins=${ADMINS}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-jar","/app.jar"]