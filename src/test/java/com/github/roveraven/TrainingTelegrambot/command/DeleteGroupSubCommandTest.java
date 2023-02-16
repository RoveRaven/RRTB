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
import org.telegram.telegrambots.meta.api.objects.Message;
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
        Long chatId = 17687L;
        Integer groupId = 8;
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(DELETE_GROUP_SUB.getCommandName()+ " 8");
        Mockito.when(message.getChatId()).thenReturn(chatId);
        update.setMessage(message);

        Mockito.when(telegramUserService.findByChatId(chatId.toString())).thenReturn(Optional.empty());
        String expectedMessage = "Group with this Id not found";

        command.execute(update);
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteGroupSub(){
        //given
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(DELETE_GROUP_SUB.getCommandName()+ " 8");
        Mockito.when(message.getChatId()).thenReturn(17687L);
        update.setMessage(message);

        List<TelegramUser> users = new ArrayList<>();
        TelegramUser user1 = new TelegramUser();
        user1.setActive(true);
        user1.setChatId(String.valueOf(17687L));
        user1.setGroupSubs(new ArrayList<>());
        users.add(user1);
        TelegramUser user2 = new TelegramUser();
        user2.setActive(true);
        user2.setChatId(String.valueOf(17L));
        user2.setGroupSubs(new ArrayList<>());
        users.add(user2);

        GroupSub groupSub = new GroupSub();
        groupSub.setTitle("g234");
        groupSub.setId(8);
        groupSub.setUsers(users);

        Mockito.when(groupSubService.findById(8)).thenReturn(Optional.of(groupSub));
        Mockito.when(telegramUserService.findByChatId("17687")).thenReturn(Optional.of(user1));
        String completeMessage = String.format("""
                        You successfully unsubscribe from group: \n\n
                        %s  -  %s""", groupSub.getId(), groupSub.getTitle());
        //when
        command.execute(update);
        //then
        users.remove(user1);
        Mockito.verify(groupSubService).save(groupSub);
        Mockito.verify(sendBotMessageService).sendMessage(message.getChatId().toString(), completeMessage );
    }

    @Test
    public void shouldProperlyReactOnCommandWithoutId() {
        //given
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(DELETE_GROUP_SUB.getCommandName());
        Mockito.when(message.getChatId()).thenReturn(17687L);
        update.setMessage(message);

        command.execute(update);

        Mockito.verify(sendBotMessageService).sendMessage(message.getChatId().toString(), "To unsubscribe from group, please, send " +
                "command like \"\\deletegroupsub N\", where N - group ID");
    }

    @Test
    public void shouldProperlyReactOnInvalidFormatId(){
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(DELETE_GROUP_SUB.getCommandName()+ " -2a");
        Mockito.when(message.getChatId()).thenReturn(17687L);
        update.setMessage(message);

        command.execute(update);

        Mockito.verify(sendBotMessageService).sendMessage(message.getChatId().toString(), "Wrong format of ID. Id must be integer and positive number");
    }
}