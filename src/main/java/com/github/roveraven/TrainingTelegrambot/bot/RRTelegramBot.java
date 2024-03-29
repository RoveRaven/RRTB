package com.github.roveraven.TrainingTelegrambot.bot;

import com.github.roveraven.TrainingTelegrambot.command.CommandContainer;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.NO;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;

@Component
public class RRTelegramBot extends TelegramLongPollingBot {
    public static String COMMAND_PREFIX = "/";
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;
    private final CommandContainer commandContainer;
    @Autowired
    public RRTelegramBot(TelegramUserService telegramUserService,
                         JavaRushGroupClient javaRushGroupClient,
                         GroupSubService groupSubService,
                         StatisticService statisticService,
                         AuthorService authorService,
                         JavaRushPostClient javaRushPostClient,
                         @Value("#{'${bot.admins}'.split(',')}") List<String> admins) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService,
                javaRushGroupClient, groupSubService, statisticService, authorService, javaRushPostClient, admins);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = getText(update).trim();
            String user = getUserName(update);
            if(message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.findCommand(commandIdentifier, user).execute(update);
            } else {
                commandContainer.findCommand(NO.getCommandName(), user).execute(update);
            }
        }
    }
}
