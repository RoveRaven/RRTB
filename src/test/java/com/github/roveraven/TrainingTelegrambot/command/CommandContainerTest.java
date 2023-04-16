package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;


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
        AuthorService authorService = Mockito.mock(AuthorService.class);
        JavaRushPostClient javaRushPostClient = Mockito.mock(JavaRushPostClient.class);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService,
                javaRushGroupClient, groupSubService, statisticService, authorService, javaRushPostClient, Collections.singletonList("admin"));
    }

    @Test
    public void shouldGetAllTheExistingCommandsForAdmin(){
        //when-then
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.findCommand(commandName.getCommandName(), "admin");
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }
    @Test
    public void shouldGetAllCommandsForNotAdminUsers(){
        //when-then
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.findCommand(commandName.getCommandName(), "user");
                    if(commandName==CommandName.STAT|| commandName==CommandName.ADMIN_HELP) {
                        Assertions.assertEquals(UnknownCommand.class, command.getClass());
                    } else {
                        Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                    }
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/vgfgfggf";
        //when
        Command command = commandContainer.findCommand(unknownCommand, "j");
        //then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

}