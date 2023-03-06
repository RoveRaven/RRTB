package com.github.roveraven.TrainingTelegrambot.command;
import com.github.roveraven.TrainingTelegrambot.annotation.AdminCommand;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.StatisticService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.*;
import static java.util.Objects.nonNull;

public class CommandContainer {
    private final ImmutableMap<String, Command> COMMAND_MAP;
    private final Command UNKNOWN_COMMAND;
    private final List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService,
                            JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService, StatisticService statisticService, List<String> admins) {
        this.admins = admins;
        COMMAND_MAP = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, statisticService))
                .put(ADD_GROUP_SUB.getCommandName(), new AddGroupSubCommand(sendBotMessageService, javaRushGroupClient, groupSubService))
                .put(LIST_GROUP_SUB.getCommandName(), new ListGroupSubCommand(telegramUserService, sendBotMessageService))
                .put(DELETE_GROUP_SUB.getCommandName(), new DeleteGroupSubCommand(sendBotMessageService, groupSubService, telegramUserService))
                .put(ADMIN_HELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .build();
        UNKNOWN_COMMAND = new UnknownCommand(sendBotMessageService);
    }

    public Command findCommand(String commandIdentifier, String userName) {
        Command command =  COMMAND_MAP.getOrDefault(commandIdentifier, UNKNOWN_COMMAND);
        if(isAdminCommand(command)) {
            if(admins.contains(userName)) {
                return command;
            } else {
                return UNKNOWN_COMMAND;
            }
        }
        return command;

    }

    private boolean isAdminCommand(Command command) {
        return nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }
}
