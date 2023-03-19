package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;
/**
 * {@link Command} for getting list of {@link GroupSub}.
 */
@Slf4j(topic = "ListGroupSubCommand")
public class ListGroupSubCommand implements Command{
    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public ListGroupSubCommand(TelegramUserService telegramUserService, SendBotMessageService sendBotMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing ListGroupSubCommand");
        Long chatId = getChatId(update);
        try {
            TelegramUser user = telegramUserService.findByChatId(chatId)
                    .orElseThrow(NotFoundException::new);


            String message = "Your active subscribes on groups: \n\n";
            String groups = user.getGroupSubs().stream()
                    .map(gs -> "Group name: " + gs.getTitle() + " ID: " + gs.getId() + "\n")
                    .collect(Collectors.joining());
            sendBotMessageService.sendMessage(chatId, message + groups);

            Instant end = Instant.now();
            log.info("ListGroupSubCommand successfully completed. Time of executing - {} milliseconds",
                    end.toEpochMilli() - start.toEpochMilli());
        }
        catch (NotFoundException e) {
            log.warn("User with id {} not registered/ didn't start the bot", chatId);
            e.printStackTrace();
        }
    }
}
