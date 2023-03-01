package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.service.*;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;


@DisplayName("Unit-level testing for CommandContainerTest")
class CommandContainerTest {
    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        JavaRushGroupClient javaRushGroupClient = Mockito.mock(JavaRushGroupClient.class);
        GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
        StatisticService statisticService = new StatisticServiceImpl(groupSubService, telegramUserService);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService,
                javaRushGroupClient, groupSubService, statisticService, new ArrayList<String>());
    }

    //@Ignore
    //@Test
    public void shouldGetAllTheExistingCommands(){
        //when-then
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.retrieveCommand(commandName.getCommandName(), "j");
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/vgfgfggf";
        //when
        Command command = commandContainer.retrieveCommand(unknownCommand, "j");
        //then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

}