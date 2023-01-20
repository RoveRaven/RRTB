package com.github.roveraven.TrainingTelegrambot.command;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.HELP;
import static com.github.roveraven.TrainingTelegrambot.command.HelpCommand.HELP_MESSAGE;
import org.junit.jupiter.api.DisplayName;
@DisplayName("Unit-level testing for HelpCommand")
class HelpCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new HelpCommand(sendBotMessageService);
    }
}