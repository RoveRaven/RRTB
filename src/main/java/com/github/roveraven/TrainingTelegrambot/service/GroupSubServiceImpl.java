package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.repository.GroupSubRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupSubServiceImpl implements GroupSubService{
    private final GroupSubRepository groupSubRepository;
    private final TelegramUserService telegramUserService;
    private final JavaRushGroupClient javaRushGroupClient;

    @Autowired
    public GroupSubServiceImpl(GroupSubRepository groupSubRepository, TelegramUserService telegramUserService, JavaRushGroupClient javaRushGroupClient) {
        this.groupSubRepository = groupSubRepository;
        this.telegramUserService = telegramUserService;
        this.javaRushGroupClient = javaRushGroupClient;

    }

    @Override
    public GroupSub save(Long chatId, GroupDiscussionInfo groupDiscussionInfo) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        if(!telegramUser.isActive()) {
            throw  new NotFoundException("""
                                            Ooops... something goes wrong. 
                                            Probably, you didn't activate the bot. 
                                            Please, try to execute the \\\"start\" command
                                            """);
        }
        //TODO add exception handling
        GroupSub groupSub;
        Optional<GroupSub> groupSubFromDB = groupSubRepository.findById(groupDiscussionInfo.getId());
        if (groupSubFromDB.isPresent()) {
            groupSub = groupSubFromDB.get();
            Optional<TelegramUser> user = groupSub.getUsers().stream()
                    .filter(u-> Objects.equals(u.getChatId(), chatId))
                    .findFirst();
            if(user.isEmpty()) {
                groupSub.addUser(telegramUser);
            } else if(Objects.equals(user.get().getChatId(), chatId) && user.get().isActive()){
                throw new NotFoundException("You have already subscribed on this group");
            } else if(Objects.equals(user.get().getChatId(), chatId) && !user.get().isActive()){
                throw new NotFoundException("""
                                                Ooops... something goes wrong. 
                                                Probably, you didn't activate the bot. 
                                                Please, try to execute the \\\"start\\\" command
                                                """);
            }
        } else {
            groupSub = new GroupSub();
            groupSub.addUser(telegramUser);
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.setTitle(groupDiscussionInfo.getTitle());
            groupSub.setLastPostId(javaRushGroupClient.getLastPostId(groupDiscussionInfo.getId()));
        }
        return groupSubRepository.save(groupSub);
    }

    @Override
    public Optional<GroupSub> findById(Integer groupId) {
        return groupSubRepository.findById(groupId);
    }

    @Override
    public List<GroupSub> findAll() {
        return groupSubRepository.findAll();
    }

    @Override
    public GroupSub save(GroupSub groupSub) {
        return groupSubRepository.save(groupSub);
    }
}
