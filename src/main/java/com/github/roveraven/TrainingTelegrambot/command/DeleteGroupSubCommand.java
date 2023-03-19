package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;

/**
 * Delete Group subscription {@link Command}.
 */
@Slf4j
public class DeleteGroupSubCommand implements   Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing DeleteGroupSubCommand, chatId = {}, message text = {}", getChatId(update), getText(update));
        String text = getText(update);
        Long chatId = getChatId(update);
        if(text.split(" ").length==1) {
            sendBotMessageService.sendMessage(chatId, "To unsubscribe from group, please, send " +
                    "command like \"\\deletegroupsub N\", where N - group ID");
            return;
        }
        String groupId = text.split(" ")[1];
        if (isNumeric(groupId)) {
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.parseInt(groupId));
            if (optionalGroupSub.isPresent()) {
                TelegramUser user = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                GroupSub groupSub = optionalGroupSub.get();
                groupSub.getUsers().remove(user);
                groupSubService.save(groupSub);
                String title = groupSubService.findById(Integer.parseInt(groupId)).get().getTitle();
                sendBotMessageService.sendMessage(chatId, String.format("""
                        You successfully unsubscribe from group: \n\n
                        %s  -  %s""", groupId, title));
                Instant end = Instant.now();
                log.info("DeleteGroupSubCommand successfully completed. Time of executing - {} milliseconds",
                        end.toEpochMilli()-start.toEpochMilli());
            } else {
                log.info("Nonexistent groupId");
                sendBotMessageService.sendMessage(chatId, "Group with this Id not found");
            }
        } else {
            log.info("GroupIf contains not numeric symbols");
            sendBotMessageService.sendMessage(chatId, "Wrong format of ID. Id must be integer and positive number");
        }
            }
        }


