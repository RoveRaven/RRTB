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
                    ✨<b>Доступные команды</b>✨

                    <b>Начать\\закончить работу с ботом</b>
                    %s - начать работу со мной
                    %s - приостановить работу со мной
                    %s - узнать количество активных пользователей бота
                    %s - получить помощь в работе со мной
                    """,
            START.getCommandName(), STOP.getCommandName(), STAT.getCommandName(), HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}