package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
/**
 * * Stop {@link Command}.
 */
public class NoCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    static final String NO_MESSAGE = "Bot supports commands, starts with(/).\n"
            + "To see the list of commands send /help";

    public NoCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), NO_MESSAGE);
    }
}