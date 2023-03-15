package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TestUtils {

    public static TelegramUser getUser(Long chatId, boolean isActive, List<GroupSub> groupSubs){
        TelegramUser user = new TelegramUser();
        user.setChatId(chatId);
        user.setActive(isActive);
        user.setGroupSubs(groupSubs);
        return user;
    }
    public static Update getUpdate(String messageText, Long chatId){
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn(messageText);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        update.setMessage(message);
        return update;
    }
    public static GroupSub getGroupSub(int id, String title){
        GroupSub groupSub = new GroupSub();
        groupSub.setId(id);
        groupSub.setTitle(title);
        return  groupSub;
    }

}
