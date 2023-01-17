package com.github.roveraven.TrainingTelegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
/**
*Command iterface for handling telegram-bot commands
*/

public interface Command {
    /**
     * Main method, which is executing command logic.
     *
     * @param update provided {@link Update} object with all the needed data for command.
     */
    void execute(Update update);
}
