package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupStatDTO;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.STAT;
import static com.github.roveraven.TrainingTelegrambot.command.StatCommand.STAT_MESSAGE;


@DisplayName("Unit-level testing for StatCommand")
class StatCommandTest{
    private SendBotMessageService sendBotMessageService;
    private StatisticService statisticService;
    private Command statCommand;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        statisticService = Mockito.mock(StatisticService.class);
        statCommand = new StatCommand(sendBotMessageService, statisticService);
    }

    @Test
    public void shouldProperlySendMessage() {
        //given
        Long chatId = 12345L;
        GroupStatDTO groupStatDTO = new GroupStatDTO(1, "g1", 1);
        StatisticDTO statisticDTO = new StatisticDTO(1, 1,
                2.5, Collections.singletonList(groupStatDTO));
        Mockito.when(statisticService.countBotStatistic()).thenReturn(statisticDTO);

        Update update = TestUtils.getUpdate(STAT.getCommandName(), chatId);
        //when
        statCommand.execute(update);
        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId, String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                String.format("%s (id - %s) -   %s subscribers",
                        groupStatDTO.getTitle(),
                        groupStatDTO.getId(),
                        groupStatDTO.getActiveUserCount())
                ));

    }
}