package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import com.github.roveraven.TrainingTelegrambot.service.AuthorService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADD_AUTHOR_SUB;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getChatId;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.getText;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Add Author subscription {@link Command}.
 */
@Slf4j(topic = "AddAuthorSubCommand")
public class AddAuthorSubCommand implements Command{
    private  final SendBotMessageService sendBotMessageService;
    private  final AuthorService authorService;
    private  final JavaRushPostClient javaRushPostClient;

    public AddAuthorSubCommand(SendBotMessageService sendBotMessageService, AuthorService authorService, JavaRushPostClient javaRushPostClient) {
        this.sendBotMessageService = sendBotMessageService;
        this.authorService = authorService;
        this.javaRushPostClient = javaRushPostClient;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing AddAuthorSubCommand, chatId = {}, message text = {}", getChatId(update), getText(update));
        Long chatId = getChatId(update);

        if(getText(update).equalsIgnoreCase(ADD_AUTHOR_SUB.getCommandName())) {
            log.info("Update included command without arguments");
            sendBotMessageService.sendMessage(chatId, "Command doesn't include author ID. To subscribe on author, retry addAuthorCommand with author ID");
            return;
        }
        String authorIdText = getText(update).split(SPACE)[1];

        if (!isNumeric(authorIdText)) {
            log.info("authorId contains not numeric symbols. authorId = {}", authorIdText);
            sendBotMessageService.sendMessage(chatId, "Invalid author ID. Please, enter correct ID");
        } else {
            Integer authorId = Integer.parseInt(authorIdText);
            Author savedAuthor = authorService.save(chatId, authorId);
            sendBotMessageService.sendMessage(chatId, String.format("You successfully subscribed on author:\n ID:  %d\n Name:  %s",
                    savedAuthor.getAuthorId(), savedAuthor.getName()));

            Instant end = Instant.now();
            log.info("AuthorSub added. Time of executing - {} milliseconds", end.toEpochMilli()-start.toEpochMilli());
            }
        }
}
