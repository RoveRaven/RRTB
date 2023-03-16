package com.github.roveraven.TrainingTelegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandUtils {
    public static String getText(Update update) {
        return update.getMessage().getText();
    }

    public static Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    public static String getUserName(Update update) {return update.getMessage().getFrom().getUserName();}
}
