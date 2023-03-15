package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.command.TestUtils;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.repository.GroupSubRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@DisplayName("Unit-level testing for GroupSubService")
public class GroupSubServiceTest {
    private GroupSubService groupSubService;
    private GroupSubRepository groupSubRepository;
    private JavaRushGroupClient javaRushGroupClient;
    private TelegramUser newUser;
    private final static Long CHAT_ID = 1L;

    @BeforeEach
    public void init(){
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        groupSubRepository = Mockito.mock(GroupSubRepository.class);
        javaRushGroupClient = Mockito.mock(JavaRushGroupClient.class);
        groupSubService = new GroupSubServiceImpl(groupSubRepository, telegramUserService, javaRushGroupClient);

        newUser = TestUtils.getUser(CHAT_ID, true, null);

        Mockito.when(telegramUserService.findByChatId(CHAT_ID)).thenReturn(Optional.of(newUser));
    }

    @Test
    public void shouldProperlySaveGroup() {
        //given
        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setId(1);
        groupDiscussionInfo.setTitle("g1");

        GroupSub expectedGroupSub = TestUtils.getGroupSub(groupDiscussionInfo.getId(), groupDiscussionInfo.getTitle());
        expectedGroupSub.setLastPostId(0);
        expectedGroupSub.addUser(newUser);
        //when
        groupSubService.save(CHAT_ID, groupDiscussionInfo);
        //then
        Mockito.verify(groupSubRepository).save(expectedGroupSub);
    }

    @Test
    public void shouldProperlyAddUserToExistingGroup() {
    //given
        TelegramUser oldTelegramUser = TestUtils.getUser(2L, true, null);

        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setId(1);
        groupDiscussionInfo.setTitle("g1");

        GroupSub groupFromDB = TestUtils.getGroupSub(groupDiscussionInfo.getId(), groupDiscussionInfo.getTitle());
        groupFromDB.addUser(oldTelegramUser);

        Mockito.when(groupSubRepository.findById(groupDiscussionInfo.getId())).thenReturn(Optional.of(groupFromDB));

        GroupSub expectedGroupSub = TestUtils.getGroupSub(groupDiscussionInfo.getId(), groupDiscussionInfo.getTitle());
        expectedGroupSub.addUser(oldTelegramUser);
        expectedGroupSub.addUser(newUser);
        //when
        groupSubService.save(CHAT_ID, groupDiscussionInfo);
        //then
        Mockito.verify(groupSubRepository).findById(groupDiscussionInfo.getId());
        Mockito.verify(groupSubRepository).save(expectedGroupSub);

    }
}
