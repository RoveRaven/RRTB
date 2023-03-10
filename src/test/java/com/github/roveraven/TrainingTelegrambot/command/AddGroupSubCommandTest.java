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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADD_GROUP_SUB;
@DisplayName("Unit-level testing for AddGroupSubCommand")
class AddGroupSubCommandTest {
    SendBotMessageService sendBotMessageService;
    GroupSubService groupSubService;
    JavaRushGroupClient groupClient;
    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        groupClient = Mockito.mock(JavaRushGroupClient.class);

    }
    @Test
    public void shouldProperlyAddGroupWithCorrectNumber() {
        //given
        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setTitle("g1");
        groupDiscussionInfo.setId(8);
        Mockito.when(groupClient.getGroupById(8)).thenReturn(groupDiscussionInfo);

        GroupSub groupSub = new GroupSub();
        groupSub.setTitle("g1");
        Mockito.when((groupSubService).save(1L, groupDiscussionInfo)).thenReturn(groupSub);

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(ADD_GROUP_SUB.getCommandName()+ " 8");
        Mockito.when(message.getChatId()).thenReturn(1L);
        update.setMessage(message);
        //Mockito.when(update.getMessage()).thenReturn(message);
        String resultMessage = "You subscribed to group: g1";

        AddGroupSubCommand addGroupSubCommand = new AddGroupSubCommand(sendBotMessageService, groupClient, groupSubService);
        //when
        addGroupSubCommand.execute(update);
        //then
        Mockito.verify(groupSubService).save(1L, groupDiscussionInfo);
        Mockito.verify(sendBotMessageService).sendMessage(1L, resultMessage);
    }

    @Test
    public void shouldProperlyReactIfGroupNotFound(){
        String notFoundMessage = "There's no group with ID \"%s\"";

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(ADD_GROUP_SUB.getCommandName()+ " 33");
        Mockito.when(message.getChatId()).thenReturn(349L);
        update.setMessage(message);

        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setTitle("g1");
        Mockito.when(groupClient.getGroupById(33)).thenReturn(groupDiscussionInfo);

        AddGroupSubCommand addGroupSubCommand = new AddGroupSubCommand(sendBotMessageService, groupClient, groupSubService);
        addGroupSubCommand.execute(update);

        Mockito.verify(sendBotMessageService).sendMessage(message.getChatId(), String.format(notFoundMessage,
                "33"));

    }

}