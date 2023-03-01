package com.github.roveraven.TrainingTelegrambot.command;

import static com.github.roveraven.TrainingTelegrambot.command.AdminHelpCommand.ADMIN_HELP_MESSAGE;
import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADMIN_HELP;
import static org.junit.jupiter.api.Assertions.*;

class AdminHelpCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return ADMIN_HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return ADMIN_HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new AdminHelpCommand(sendBotMessageService);
    }
}