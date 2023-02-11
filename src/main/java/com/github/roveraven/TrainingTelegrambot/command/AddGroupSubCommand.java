package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupRequestsArgs;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.service.GroupSubService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.ADD_GROUP_SUB;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;


/**
 * Add Group subscription {@link Command}.
 */
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
        if(update.getMessage().getText().equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())) {
            sendGroupIdList(update.getMessage().getChatId().toString());
            return;
        }
        String groupId = update.getMessage().getText().split(SPACE)[1];
        String chatId = update.getMessage().getChatId().toString();
        if (isNumeric(groupId)) {
            GroupDiscussionInfo groupById = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
            if(isNull(groupById.getId())) {
                sendGroupNotFound(chatId, groupId);
            }
            GroupSub savedGroupSub;
            try{
                savedGroupSub = groupSubService.save(chatId, groupById);
                sendBotMessageService.sendMessage(chatId, String.format("You subscribed to group: %s", savedGroupSub.getTitle()));
            }
            catch (NotFoundException e) {
                sendBotMessageService.sendMessage(chatId, "Ooops... something goes wrong. Probably, you didn't activate the bot. Please, try to execute the \"start\" command");
            }

        }
        else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        String notFoundMessage = "There's no group wirh ID \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(notFoundMessage, groupId));
    }

    private void sendGroupIdList(String chatId){
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
