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
export BOT_NAME=test_rrtb_bot
export BOT_TOKEN=5561589568:AAFBIobS9e0Qu6xBw_KanymKV0AcuqbQY-M

# Start new deployment
docker-compose up --build -d