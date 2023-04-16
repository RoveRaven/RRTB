package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.AuthorService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Optional;

import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getChatId;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getText;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Slf4j
public class DeleteAuthorSubCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final AuthorService authorService;
    private final TelegramUserService telegramUserService;

    public DeleteAuthorSubCommand(SendBotMessageService sendBotMessageService, AuthorService authorService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorService = authorService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing DeleteAuthorSubCommand, chatId = {}, message text = {}", getChatId(update), getText(update));
        String text = getText(update);
        Long chatId = getChatId(update);
        if(text.split(" ").length==1) {
            sendBotMessageService.sendMessage(chatId, "To unsubscribe from author, please, send " +
                    "command like \"\\deleteauthorsub N\", where N - group ID");
            return;
        }
        String authorId = text.split(" ")[1];
        if (isNumeric(authorId)) {
            Optional<Author> optionalGroupSub = authorService.findById(Integer.parseInt(authorId));
            if (optionalGroupSub.isPresent()) {
                TelegramUser user = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                Author author = optionalGroupSub.get();
                author.getUsers2().remove(user);
                authorService.save(author);
                String authorName = authorService.findById(Integer.parseInt(authorId)).get().getName();
                sendBotMessageService.sendMessage(chatId, String.format("""
                        You successfully unsubscribe from author: \n\n
                        %s  -  %s""", authorId, authorName));
                Instant end = Instant.now();
                log.info("DeleteAuthorSubCommand successfully completed. Time of executing - {} milliseconds",
                        end.toEpochMilli()-start.toEpochMilli());
            } else {
                log.info("Nonexistent authorId");
                sendBotMessageService.sendMessage(chatId, "Author with this Id not found");
            }
        } else {
            log.info("AuthorId contains not numeric symbols");
            sendBotMessageService.sendMessage(chatId, "Wrong format of ID. Id must be integer and positive number");
        }

    }
}
