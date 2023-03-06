package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;
/**
 * {@link Command} for getting list of {@link GroupSub}.
 */
public class ListGroupSubCommand implements Command{
    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public ListGroupSubCommand(TelegramUserService telegramUserService, SendBotMessageService sendBotMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        //todo add exception handling
        TelegramUser user = telegramUserService.findByChatId(update.getMessage().getChatId())
                .orElseThrow(NotFoundException::new);
        String message = "Your active subscribes on groups: \n\n";
        String groups = user.getGroupSubs().stream()
                .map(gs-> "Group name: " + gs.getTitle() + " ID: " + gs.getId() + "\n")
                .collect(Collectors.joining());
        sendBotMessageService.sendMessage(chatId, message + groups);
    }
}
