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
@DisplayName("Unit-level testing for AddGroupSubCommand")
class AddGroupSubCommandTest {
    SendBotMessageService sendBotMessageService;
    GroupSubService groupSubService;
    JavaRushGroupClient groupClient;
    AddGroupSubCommand addGroupSubCommand;
    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        groupClient = Mockito.mock(JavaRushGroupClient.class);
        addGroupSubCommand = new AddGroupSubCommand(sendBotMessageService, groupClient, groupSubService);

    }
    @Test
    public void shouldProperlyAddGroupWithCorrectNumber() {
        //given
        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setTitle("g1");
        groupDiscussionInfo.setId(8);
        Mockito.when(groupClient.getGroupById(8)).thenReturn(groupDiscussionInfo);

        GroupSub groupSub = TestUtils.getGroupSub(8, "g1");

        Mockito.when((groupSubService).save(1L, groupDiscussionInfo)).thenReturn(groupSub);

        Update update = TestUtils.getUpdate(ADD_GROUP_SUB.getCommandName()+ " 8", 1L);
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

        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setTitle("g1");
        Mockito.when(groupClient.getGroupById(33)).thenReturn(groupDiscussionInfo);
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(update.getMessage().getChatId(), String.format(notFoundMessage,
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
        Update update = TestUtils.getUpdate(ADD_GROUP_SUB.getCommandName(), 1L);
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(update.getMessage().getChatId(),
                message);
    }
}