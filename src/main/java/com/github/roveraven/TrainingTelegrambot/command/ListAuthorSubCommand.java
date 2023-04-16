package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getChatId;
/**
 * {@link Command} for getting list of {@link Author}.
 */
@Slf4j(topic = "ListAuthorSubCommand")
public class ListAuthorSubCommand implements Command{
    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public ListAuthorSubCommand(TelegramUserService telegramUserService, SendBotMessageService sendBotMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing ListAuthorSubCommand");

        Long chatId = getChatId(update);
        try {
            TelegramUser user = telegramUserService.findByChatId(chatId)
                    .orElseThrow(NotFoundException::new);
            String message = "Your active subscribes on authors: \n\n";
            String authors = user.getAuthors().stream()
                    .map(author -> "Author name: " + author.getName() + " ID: " + author.getAuthorId() + "\n")
                    .collect(Collectors.joining());
            sendBotMessageService.sendMessage(chatId, message + authors);

            Instant end = Instant.now();
            log.info("ListAuthorSubCommand successfully completed. Time of executing - {} milliseconds",
                    end.toEpochMilli() - start.toEpochMilli());
        }
        catch (NotFoundException e) {
            log.warn("User with id {}  didn't start the bot", chatId);
            e.printStackTrace();
        }
    }
}
