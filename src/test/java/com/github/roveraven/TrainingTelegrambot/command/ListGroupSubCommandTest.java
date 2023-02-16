package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static com.github.roveraven.TrainingTelegrambot.command.CommandName.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DisplayName("Unit-level testing for ListGroupSubCommand")
class ListGroupSubCommandTest {
    @Test
    public void shouldProperlyGetGroupSubList() {
        //given
        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(createGroupSub(1, "Group1"));
        groupSubList.add(createGroupSub(2, "Group2"));
        groupSubList.add(createGroupSub(3, "Group3"));
        groupSubList.add(createGroupSub(4, "Group4"));

        TelegramUser user = new TelegramUser();
        user.setActive(true);
        user.setChatId("1");
        user.setGroupSubs(groupSubList);

        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

        Mockito.when(telegramUserService.findByChatId(user.getChatId())).thenReturn(Optional.of(user));

        ListGroupSubCommand listGroupSubCommand = new ListGroupSubCommand(telegramUserService, sendBotMessageService);

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(LIST_GROUP_SUB.getCommandName());
        Mockito.when(message.getChatId()).thenReturn(Long.valueOf(user.getChatId()));
        update.setMessage(message);

        String groups = "Your active subscribes on groups: \n\n" +
                user.getGroupSubs().stream()
                        .map(gs-> "Group name: " + gs.getTitle() + " ID: " + gs.getId() + "\n")
                        .collect(Collectors.joining());
        //when
        listGroupSubCommand.execute(update);
        Mockito.verify(sendBotMessageService).sendMessage(user.getChatId(), groups);

    }


    private GroupSub createGroupSub(Integer id, String title) {
        GroupSub groupSub = new GroupSub();
        groupSub.setId(id);
        groupSub.setTitle(title);
        return groupSub;
    }

}