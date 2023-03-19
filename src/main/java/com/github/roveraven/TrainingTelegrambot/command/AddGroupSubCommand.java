package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupRequestsArgs;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADD_GROUP_SUB;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static com.github.roveraven.TrainingTelegrambot.command.CommandUtils.*;

/**
 * Add Group subscription {@link Command}.
 */
@Slf4j(topic = "AddGroupSubCommand")
public class AddGroupSubCommand implements Command{
    private  final SendBotMessageService sendBotMessageService;
    private final JavaRushGroupClient javaRushGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService,
                              JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.javaRushGroupClient = javaRushGroupClient;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        Instant start = Instant.now();
        log.info("Start executing AddGroupSubCommand, chatId = {}, message text = {}", getChatId(update), getText(update));
        if(getText(update).equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())) {
            log.info("Update included command without arguments");
            sendGroupIdList(getChatId(update));
            return;
        }
        String groupId = getText(update).split(SPACE)[1];
        Long chatId = getChatId(update);
        if (isNumeric(groupId)) {
            GroupDiscussionInfo groupById = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
            if(isNull(groupById)||isNull(groupById.getId())) {
                log.warn("GroupSub with id {} not found", groupId);
                sendGroupNotFound(chatId, groupId);
                return;
            }
            GroupSub savedGroupSub;
            try{
                savedGroupSub = groupSubService.save(chatId, groupById);
                sendBotMessageService.sendMessage(chatId, String.format("You subscribed to group: %s", savedGroupSub.getTitle()));
                Instant end = Instant.now();
                log.info("GroupSub added. Time of executing - {} milliseconds", end.toEpochMilli()-start.toEpochMilli());
            }
            catch (NotFoundException e) {
                log.warn("Error on step of saving GroupSub", e);
                    sendBotMessageService.sendMessage(chatId, e.getMessage());
            }

        }
        else {
            log.info("groupId contains not numeric symbols. groupId = {}", groupId);
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupNotFound(Long chatId, String groupId) {
        String notFoundMessage = "There's no group with ID \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(notFoundMessage, groupId));
    }

    private void sendGroupIdList(Long chatId){
        String gropIds = javaRushGroupClient.getGroupArgs(GroupRequestsArgs.builder().build())
                .stream()
                .map(g->String.format("%s - %s\n", g.getId(),g.getTitle()))
                .collect(Collectors.joining());
        String message = """
                To subscribe on grop - send command with group ID. \n
                Example: /AddGroupSub 16 \n\n
                Full list of groups:\n
                GroupID - GroupName\n
                %s""";
        sendBotMessageService.sendMessage(chatId, String.format(message, gropIds));
    }
}
