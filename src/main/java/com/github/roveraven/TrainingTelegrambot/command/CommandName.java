package com.github.roveraven.TrainingTelegrambot.command;

public enum CommandName {
     /**
     * Enumeration for {@link Command}'s.
     */
     START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO("/no"),
    STAT("/stat"),
    ADD_GROUP_SUB("/addgroupsub"),
    LIST_GROUP_SUB("/listgroupsub"),
    LIST_AUTHOR_SUB("/listauthorsub"),
    DELETE_GROUP_SUB("/deletegroupsub"),
    ADD_AUTHOR_SUB("/addauthorsub"),
    ADMIN_HELP("/ahelp"),
    DELETE_AUTHOR_SUB("/deleteauthorsub");



    private final String commandName;


    CommandName(String commandName) {
        this.commandName = commandName;
    }
    public String getCommandName() {
        return commandName;
    }
}
