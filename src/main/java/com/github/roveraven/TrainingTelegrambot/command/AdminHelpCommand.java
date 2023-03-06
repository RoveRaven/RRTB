package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.annotation.AdminCommand;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADMIN_HELP;

/**
 * Admin Help {@link Command}.
 */
@AdminCommand
public class AdminHelpCommand implements Command{
    public static final String ADMIN_HELP_MESSAGE = String.format("✨<b>Available admin commands</b>✨\n\n"
                    + "<b>Get statistics</b>\n"
                    + "%s - bot statistic\n",
            ADMIN_HELP.getCommandName());
    private final SendBotMessageService sendBotMessageService;

    public AdminHelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), ADMIN_HELP_MESSAGE);
    }
}
