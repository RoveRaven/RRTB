package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.annotation.AdminCommand;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getChatId;

@AdminCommand
@Slf4j( topic = "StatCommand")
public class StatCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final StatisticService statisticService;
    static final String  STAT_MESSAGE = """
            ✨<b>Statistics prepared:</b>✨
            - Count of active users: %s
            - Count of inactive users: %s
            - Average number groups per user: %s

            <b>Info about active groups</b>:
            %s""";

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, StatisticService statisticService) {
        this.sendBotMessageService = sendBotMessageService;
        this.statisticService = statisticService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing StatCommand");
        StatisticDTO statisticDTO = statisticService.countBotStatistic();

        String groupsInfo = statisticDTO.getGroupStatDTOs().stream()
                .map(it -> String.format("%s (id - %s) -   %s subscribers",
                        it.getTitle(),
                        it.getId(),
                        it.getActiveUserCount()))
                .collect(Collectors.joining("\n"));
        sendBotMessageService.sendMessage(getChatId(update), String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                groupsInfo));
        Instant end = Instant.now();
        log.info("StatCommand successfully completed. Time of executing - {} milliseconds",
                end.toEpochMilli() - start.toEpochMilli());
    }
}
