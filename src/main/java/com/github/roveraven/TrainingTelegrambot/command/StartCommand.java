package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;
/**
 * * Start {@link Command}.
 */
@Slf4j(topic  = "StartCommand")
public class StartCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    static final String START_MESSAGE = "Bot started";


    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }



    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing StartCommand");
        Long chatId = getChatId(update);

        telegramUserService.findByChatId(chatId).ifPresentOrElse(
                user -> {
                    user.setActive(true);
                    telegramUserService.save(user);
                },
                () -> {
                    TelegramUser user = new TelegramUser();
                    user.setChatId(chatId);
                    user.setActive(true);
                    telegramUserService.save(user);
                }
        );
        Instant end = Instant.now();
        log.info("StartCommand successfully completed. Time of executing - {} milliseconds",
                end.toEpochMilli() - start.toEpochMilli());
        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }
}
