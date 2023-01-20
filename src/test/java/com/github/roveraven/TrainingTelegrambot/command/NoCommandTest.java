package com.github.roveraven.TrainingTelegrambot.command;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.NO;
import static com.github.roveraven.TrainingTelegrambot.command.NoCommand.NO_MESSAGE;

import org.junit.jupiter.api.DisplayName;
@DisplayName("Unit-level testing for NoCommand")
class NoCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
      return  NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NoCommand(sendBotMessageService);
    }
}