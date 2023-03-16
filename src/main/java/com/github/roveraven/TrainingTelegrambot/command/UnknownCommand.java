package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;
/**
 * * Stop {@link Command}.
 */
public class UnknownCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    static final String UNKNOWN_MESSAGE = "Unknown command \uD83D\uDE1F, send /help to get list of available commands.";

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(getChatId(update), UNKNOWN_MESSAGE);
    }
}