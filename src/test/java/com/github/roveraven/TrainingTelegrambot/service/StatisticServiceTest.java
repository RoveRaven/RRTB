package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.command.TestUtils;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupStatDTO;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

@DisplayName("Unit-level testing for StatisticsService")
class StatisticServiceTest {
    private GroupSubService groupSubService;
    private TelegramUserService telegramUserService;
    private StatisticService statisticService;

    @BeforeEach
    public void init() {
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        statisticService = new StatisticServiceImpl(groupSubService, telegramUserService);
    }

    @Test
    public void shouldProperlySendStatDTO() {
        //given
        Mockito.when(telegramUserService.findAllInactiveUsers()).thenReturn(Collections.singletonList(new TelegramUser()));
        TelegramUser activeUser = new TelegramUser();
        activeUser.setGroupSubs(Collections.singletonList(new GroupSub()));
        Mockito.when(telegramUserService.findAllActiveUsers()).thenReturn(Collections.singletonList(activeUser));
        GroupSub groupSub = TestUtils.getGroupSub(1, "g1");
        groupSub.setUsers(Collections.singletonList(new TelegramUser()));
        Mockito.when(groupSubService.findAll()).thenReturn(Collections.singletonList(groupSub));
        //when
        StatisticDTO statisticDTO = statisticService.countBotStatistic();
        //then
        Assertions.assertNotNull(statisticDTO);
        Assertions.assertEquals(1, statisticDTO.getActiveUserCount());
        Assertions.assertEquals(1, statisticDTO.getInactiveUserCount());
        Assertions.assertEquals(1.0, statisticDTO.getAverageGroupCountByUser());
        Assertions.assertEquals(Collections.singletonList(new GroupStatDTO(groupSub.getId(), groupSub.getTitle(), groupSub.getUsers().size()))
                , statisticDTO.getGroupStatDTOs());
    }

}