package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;
/**
 * * Stop {@link Command}.
 */
@Slf4j(topic = "StopCommand")
public class StopCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    static final String STOP_MESSAGE = "Bot stopped \uD83D\uDE1F.";

    public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing StartCommand");
        Long chatId = getChatId(update);
        sendBotMessageService.sendMessage(chatId, STOP_MESSAGE);
        telegramUserService.findByChatId(chatId)
                .ifPresent(
                        user -> {
                            user.setActive(false);
                            telegramUserService.save(user);
                        }
                );
        Instant end = Instant.now();
        log.info("StopCommand successfully completed. Time of executing - {} milliseconds",
                end.toEpochMilli() - start.toEpochMilli());
    }
}