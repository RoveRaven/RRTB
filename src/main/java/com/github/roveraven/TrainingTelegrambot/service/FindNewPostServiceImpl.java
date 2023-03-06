package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewPostServiceImpl implements FindNewPostService {
    private final String JAVARUSH_API_FORMAT = "https://javarush.com/api/1.0/rest/posts/%s";
    private final GroupSubService groupSubService;
    private final JavaRushPostClient postClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewPostServiceImpl(GroupSubService groupSubService, JavaRushPostClient postClient, SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.postClient = postClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewPosts() {
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = postClient.findNewPosts(groupSub.getId(), groupSub.getLastPostId());
            setNewLastPostId(groupSub, newPosts);
            notifySubscribersAboutNewPosts(groupSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewPosts(GroupSub gs, List<PostInfo> posts) {
        Collections.reverse(posts);
        List<String> messagesWithNewPosts = posts.stream()
                .map(post -> String.format("""
                        There is new post: \n  <b>%s</b> \n in group:  <b>%s</b>
                        <b>Description:  </b> %s \n\n
                        <b>Reference: </b> %s""", post.getTitle(), gs.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .collect(Collectors.toList());
        gs.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewPosts));
    }

    private void setNewLastPostId(GroupSub groupSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    groupSub.setLastPostId(id);
                    groupSubService.save(groupSub);
                });
    }

    private String getPostUrl(String key) {
        return String.format(JAVARUSH_API_FORMAT, key);
    }
}

