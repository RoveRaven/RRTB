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
public class FindNewArticleServiceImpl implements FindNewArticleService{
    private final String JAVARUSH_API_FORMAT = "https://javarush.com/api/1.0/rest/posts/%s";
    private final GroupSubService groupSubService;
    private final JavaRushPostClient postClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewArticleServiceImpl(GroupSubService groupSubService, JavaRushPostClient postClient, SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.postClient = postClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewArticles() {
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = postClient.findNewPosts(groupSub.getId(), groupSub.getLastArticleId());
            setNewLastArticleId(groupSub, newPosts);
            notifySubscribersAboutNewArticles(groupSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewArticles(GroupSub gs, List<PostInfo> posts) {
        Collections.reverse(posts);
        List<String> messagesWithNewArticles = posts.stream()
                .map(post -> String.format("""
                        There is new article: \n  <b>%s</b> \n in group:  <b>%s</b>
                        <b>Description:  </b> %s \n\n
                        <b>Reference: </b> %s""", post.getTitle(), gs.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .collect(Collectors.toList());
        gs.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewArticles));
    }

    private void setNewLastArticleId(GroupSub groupSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    groupSub.setLastArticleId(id);
                    groupSubService.save(groupSub);
                });
    }

    private String getPostUrl(String key) {
        return String.format(JAVARUSH_API_FORMAT, key);
    }
}

