package com.github.roveraven.TrainingTelegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.STAT;
import static com.github.roveraven.TrainingTelegrambot.command.StatCommand.STAT_MESSAGE;


@DisplayName("Unit-level testing for StatCommand")
class StatCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return  STAT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return String.format(STAT_MESSAGE, 0);
    }

    @Override
    Command getCommand() {
        return new StatCommand(sendBotMessageService, telegramUserService);
    }
}