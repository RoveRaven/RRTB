package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.DELETE_GROUP_SUB;
@DisplayName("Unit-level testing for DeleteGroupSubCommand")
class DeleteGroupSubCommandTest {
    SendBotMessageService sendBotMessageService;
    GroupSubService groupSubService;
    TelegramUserService telegramUserService;
    private Command command;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        command = new DeleteGroupSubCommand(sendBotMessageService, groupSubService, telegramUserService);
    }

    @Test
    public void shouldProperlyReactOnUnexistingId() {
        //given
        Long chatId = 17687L;
        Integer groupId = 8;
        Update update = TestUtils.getUpdate(DELETE_GROUP_SUB.getCommandName()+ " 8", chatId);

        Mockito.when(telegramUserService.findByChatId(chatId)).thenReturn(Optional.empty());
        String expectedMessage = "Group with this Id not found";
        //when
        command.execute(update);
        //then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteGroupSub(){
        //given
        Update update = TestUtils.getUpdate(DELETE_GROUP_SUB.getCommandName()+ " 8", 17687L);

        List<TelegramUser> users = new ArrayList<>();
        TelegramUser user1 = TestUtils.getUser(17687L, true, new ArrayList<>());
        users.add(user1);
        TelegramUser user2 = TestUtils.getUser(17L, true, new ArrayList<>());
        users.add(user2);

        GroupSub groupSub = TestUtils.getGroupSub(8, "g234");
        groupSub.setUsers(users);

        Mockito.when(groupSubService.findById(8)).thenReturn(Optional.of(groupSub));
        Mockito.when(telegramUserService.findByChatId(17687L)).thenReturn(Optional.of(user1));
        String completeMessage = String.format("""
                        You successfully unsubscribe from group: \n\n
                        %s  -  %s""", groupSub.getId(), groupSub.getTitle());
        //when
        command.execute(update);
        //then
        users.remove(user1);
        Mockito.verify(groupSubService).save(groupSub);
        Mockito.verify(sendBotMessageService).sendMessage(update.getMessage().getChatId(), completeMessage );
    }

    @Test
    public void shouldProperlyReactOnCommandWithoutId() {
        //given
        Update update = TestUtils.getUpdate(DELETE_GROUP_SUB.getCommandName(), 17687L);
        //when
        command.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(update.getMessage().getChatId(),
                "To unsubscribe from group, please, send " +
                "command like \"\\deletegroupsub N\", where N - group ID");
    }

    @Test
    public void shouldProperlyReactOnInvalidFormatId(){
        //given
        Update update = TestUtils.getUpdate(DELETE_GROUP_SUB.getCommandName()+ " -2a", 17687L);
        //when
        command.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(update.getMessage().getChatId(),
                "Wrong format of ID. Id must be integer and positive number");
    }
}