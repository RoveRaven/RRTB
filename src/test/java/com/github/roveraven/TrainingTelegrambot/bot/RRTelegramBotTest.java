package com.github.roveraven.TrainingTelegrambot.bot;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.StatisticService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.Optional;
@DisplayName("Unit-level test for RRTelegramBot")
class RRTelegramBotTest {
    private RRTelegramBot rrTelegramBot;
    private GroupSubService groupSubService;
    GroupDiscussionInfo groupDiscussionInfo= new GroupDiscussionInfo();
    String command = "/addgroupsub 16";
    String userName = "userName";
    User user = Mockito.mock(User.class);

    @BeforeEach
    public void init(){
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        JavaRushGroupClient javaRushGroupClient = Mockito.mock(JavaRushGroupClient.class);
        Mockito.when(javaRushGroupClient.getGroupById(16)).thenReturn(groupDiscussionInfo);
        groupSubService = Mockito.mock(GroupSubService.class);
        Mockito.when(groupSubService.findById(16)).thenReturn(Optional.of(new GroupSub()));
        Mockito.when(groupSubService.save(1L, groupDiscussionInfo)).thenReturn(new GroupSub());
        StatisticService statisticService = Mockito.mock(StatisticService.class);
        Mockito.when(user.getUserName()).thenReturn(userName);

        rrTelegramBot = new RRTelegramBot(telegramUserService, javaRushGroupClient, groupSubService,
                statisticService, Collections.singletonList("userName"));
        groupDiscussionInfo.setId(16);


    }
    @Test
    public void shouldProperlyProceedMessageWithCommandName() {
        //given
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(command);
        Mockito.when(message.getChatId()).thenReturn(1L);
        Mockito.when(message.getFrom()). thenReturn(user);
        Mockito.when(message.hasText()).thenReturn(true);
        update.setMessage(message);
        //when
        rrTelegramBot.onUpdateReceived(update);
        //then
        Mockito.verify(groupSubService).save(1L, groupDiscussionInfo);
    }
}