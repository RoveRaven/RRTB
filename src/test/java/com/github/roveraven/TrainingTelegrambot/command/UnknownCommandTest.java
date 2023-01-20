package com.github.roveraven.TrainingTelegrambot.command;


import org.junit.jupiter.api.DisplayName;

import static com.github.roveraven.TrainingTelegrambot.command.UnknownCommand.UNKNOWN_MESSAGE;
@DisplayName("Unit-level testing for UnknownCommand")
class UnknownCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return  "/jgccjvhv";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}