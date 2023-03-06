package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.annotation.AdminCommand;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.StatisticService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

@AdminCommand
public class StatCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final StatisticService statisticService;
    static final String  STAT_MESSAGE = "✨<b>Statistics prepared:</b>✨\n" +
            "- Count of active users: %s\n" +
            "- Count of inactive users: %s\n" +
            "- Average number groups per user: %s\n\n" +
            "<b>Info about active groups</b>:\n" +
            "%s";

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, StatisticService statisticService) {
        this.sendBotMessageService = sendBotMessageService;
        this.statisticService = statisticService;
    }

    @Override
    public void execute(Update update) {
        StatisticDTO statisticDTO = statisticService.countBotStatistic();

        String groupsInfo = statisticDTO.getGroupStatDTOs().stream()
                .map(it -> String.format("%s (id - %s) -   %s subscribers",
                        it.getTitle(),
                        it.getId(),
                        it.getActiveUserCount()))
                .collect(Collectors.joining("\n"));
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                groupsInfo));
    }
}
