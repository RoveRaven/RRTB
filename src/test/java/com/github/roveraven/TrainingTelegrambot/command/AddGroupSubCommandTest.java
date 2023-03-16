package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADD_GROUP_SUB;
import static com.github.roveraven.TrainingTelegrambot.command.TestUtils.*;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;
@DisplayName("Unit-level testing for AddGroupSubCommand")
class AddGroupSubCommandTest {
    SendBotMessageService sendBotMessageService;
    GroupSubService groupSubService;
    JavaRushGroupClient javaRushGroupClient;
    AddGroupSubCommand addGroupSubCommand;
    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        javaRushGroupClient = Mockito.mock(JavaRushGroupClient.class);
        addGroupSubCommand = new AddGroupSubCommand(sendBotMessageService, javaRushGroupClient, groupSubService);

    }
    @Test
    public void shouldProperlyAddGroupWithCorrectNumber() {
        //given
        GroupDiscussionInfo groupDiscussionInfo = getGroupDiscussionInfo(8, "g1");
        Mockito.when(javaRushGroupClient.getGroupById(8)).thenReturn(groupDiscussionInfo);

        GroupSub groupSub = getGroupSub(8, "g1");

        Mockito.when((groupSubService).save(1L, groupDiscussionInfo)).thenReturn(groupSub);

        Update update = getUpdate(ADD_GROUP_SUB.getCommandName()+ " 8", 1L);
        String resultMessage = "You subscribed to group: g1";
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(groupSubService).save(1L, groupDiscussionInfo);
        Mockito.verify(sendBotMessageService).sendMessage(1L, resultMessage);
    }

    @Test
    public void shouldProperlyReactIfGroupNotFound(){
        //given
        String notFoundMessage = "There's no group with ID \"%s\"";

        Update update = TestUtils.getUpdate(ADD_GROUP_SUB.getCommandName()+ " 33", 349L);

        Mockito.when(javaRushGroupClient.getGroupById(33)).thenReturn(null);
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(getChatId(update), String.format(notFoundMessage,
                "33"));
    }

    @Test
    public void shouldProperlyReturnGroupSubsList() {
        //given
        String message = """
                To subscribe on grop - send command with group ID. \n
                Example: /AddGroupSub 16 \n\n
                Full list of groups:\n
                GroupID - GroupName\n
                """;
        Update update = getUpdate(ADD_GROUP_SUB.getCommandName(), 1L);
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(getChatId(update),
                message);
    }
}