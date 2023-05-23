# RRTB
Training Telegram Bot
forked from https://github.com/javarushcommunity/javarush-telegrambot

## Deployment
Deployment process as easy as possible:
Required software:
- terminal for running bash scripts
- docker
- docker-compose

to deploy application, uncomment strings 13-15, switch to needed branch and run bash script:

$ bash start.sh ${bot_username} ${bot_token} ${admins_list}

or  wright these parameters directly in the script and run it;

That's all.

Also you can improve bot's inteface using Telegram BotFather.
To to embedd bot's commands into Telegram, follow the instruction: 
*Text to BotFather /mybots
*choose your bot
*tap "Edit Commands" button
*copy prepared text from COMMANDS_BOT_FATHER file in project root and send it
*done!
