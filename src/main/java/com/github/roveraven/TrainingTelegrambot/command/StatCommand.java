package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StatCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    static final String  STAT_MESSAGE = "RRTelegramBot использует %s человек.";

    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        int activeUsersCount = telegramUserService.retrieveAllActiveUsers().size();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(STAT_MESSAGE, activeUsersCount));
    }
}
