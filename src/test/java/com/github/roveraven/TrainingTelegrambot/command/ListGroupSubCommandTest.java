package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.LIST_GROUP_SUB;

@DisplayName("Unit-level testing for ListGroupSubCommand")
class ListGroupSubCommandTest {
    @Test
    public void shouldProperlyGetGroupSubList() {
        //given
        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(TestUtils.getGroupSub(1, "Group1"));
        groupSubList.add(TestUtils.getGroupSub(2, "Group2"));
        groupSubList.add(TestUtils.getGroupSub(3, "Group3"));
        groupSubList.add(TestUtils.getGroupSub(4, "Group4"));

        TelegramUser user = TestUtils.getUser(1L, true, groupSubList);

        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

        Mockito.when(telegramUserService.findByChatId(user.getChatId())).thenReturn(Optional.of(user));

        ListGroupSubCommand listGroupSubCommand = new ListGroupSubCommand(telegramUserService, sendBotMessageService);

        Update update = TestUtils.getUpdate(LIST_GROUP_SUB.getCommandName(), user.getChatId());

        String groups = "Your active subscribes on groups: \n\n" +
                user.getGroupSubs().stream()
                        .map(gs-> "Group name: " + gs.getTitle() + " ID: " + gs.getId() + "\n")
                        .collect(Collectors.joining());
        //when
        listGroupSubCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(user.getChatId(), groups);

    }
}