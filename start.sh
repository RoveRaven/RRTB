#!/bin/bash

# Pull new changes
git pull

# Prepare Jar
mvn clean
mvn package

# Ensure, that docker-compose stopped
docker-compose stop

# Add environment variables
#export BOT_NAME=$1
#export BOT_TOKEN=$2
#export ADMINS=$3
export BOT_NAME=!!!write_your_bot_name_here!!!
export BOT_TOKEN=!!!write_your_bot_token_here!!!
export ADMINS=!!!write_admins_usernames_here!!!
export BOT_DB_USERNAME=rrtb_db_user
export BOT_DB_PASSWORD=password

# Start new deployment
docker-compose up --build -d