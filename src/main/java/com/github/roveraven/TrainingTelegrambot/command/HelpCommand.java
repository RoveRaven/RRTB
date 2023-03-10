package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.*;

/**
 * * Start {@link Command}.
 */
public class HelpCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    static final String HELP_MESSAGE = String.format("""
                    ✨<b>Actual commands</b>✨

                    <b>Start\\end work with bot</b>
                    %s - start bot
                    %s - stop bot
                    %s - get help about bots's work
                    %s - subscribe on group
                    %s - list of your subscribes
                    %s - delete subscribe
                    """,
            START.getCommandName(), STOP.getCommandName(),
            HELP.getCommandName(), ADD_GROUP_SUB.getCommandName(), LIST_GROUP_SUB.getCommandName(),
            DELETE_GROUP_SUB.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
    }
}