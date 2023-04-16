package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FindNewPostServiceImpl implements FindNewPostService {
    private final String JAVARUSH_API_FORMAT = "https://javarush.com/api/1.0/rest/posts/%s";
    private final GroupSubService groupSubService;
    private final AuthorService authorService;
    private final JavaRushPostClient javaRushPostClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewPostServiceImpl(GroupSubService groupSubService, AuthorService authorService, JavaRushPostClient postClient, SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.authorService = authorService;
        this.javaRushPostClient = postClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewPosts() {
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = javaRushPostClient.findNewPosts(groupSub.getId(), groupSub.getLastPostId());
            setNewLastPostIdForGroupSub(groupSub, newPosts);
            notifyGroupSubscribersAboutNewPosts(groupSub, newPosts);
        });
        authorService.findAll().forEach(author -> {
            List<PostInfo> newPostsForAuthor = javaRushPostClient
                    .findNewPostsForAllGroups()
                    .stream()
                    .filter(p -> Objects.equals(p.getAuthorInfo().getUserId(), author.getAuthorId()))
                    .filter(p -> p.getId()>author.getLastPostId())
                    .collect(Collectors.toList());
            if(!newPostsForAuthor.isEmpty()) {
                setNewLastPostIdForAuthor(author, newPostsForAuthor);
                notifyAuthorSubscribersAboutNewPosts(author, newPostsForAuthor);
            }
        });
    }

    private void notifyGroupSubscribersAboutNewPosts(GroupSub gs, List<PostInfo> posts) {
        Collections.reverse(posts);
        List<String> messagesWithNewPosts = posts.stream()
                .map(post -> String.format("""
                        There is new post: \n <b>%s</b> \n
                        In <b>group</b>:  <b>%s</b>
                        From author: <b>%s</b> with ID: <b>%d</b>
                        <b>Description:  </b>%s \n\n
                        <b>Reference: </b> %s""",
                        post.getTitle(),
                        gs.getTitle(),
                        getAuthorName(post),
                        getAuthorId(post),
                        post.getDescription(),
                        getPostUrl(post.getKey())))
                .collect(Collectors.toList());
        gs.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewPosts));
    }

    private void notifyAuthorSubscribersAboutNewPosts(Author author, List<PostInfo> posts) {
        Collections.reverse(posts);
        List<String> messagesWithNewPosts = posts.stream()
                .map(post -> String.format("""
                        There is new post: \n <b>%s</b> \n
                        In group:  <b>%s</b>
                        From <b>author</b>: <b>%s</b> with ID: <b>%d</b>
                        <b>Description:  </b>%s \n\n
                        <b>Reference: </b> %s""",
                        post.getTitle(),
                        post.getGroupInfo().getTitle(),
                        getAuthorName(post),
                        getAuthorId(post),
                        post.getDescription(),
                        getPostUrl(post.getKey())))
                .collect(Collectors.toList());
        List<Integer> groupSubIds = posts.stream().map(p -> p.getGroupInfo().getId()).toList();
        author.getUsers2().stream()
                .filter(TelegramUser::isActive)
                .filter(user -> user.getGroupSubs().stream().noneMatch(gs -> groupSubIds.contains(gs.getId())))       //check that user isn't subscribed on group, where post was published, to prevent double notifying.
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewPosts));
    }

    private void setNewLastPostIdForGroupSub(GroupSub groupSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    groupSub.setLastPostId(id);
                    groupSubService.save(groupSub);
                });
    }

    private void setNewLastPostIdForAuthor(Author author, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    author.setLastPostId(id);
                    authorService.save(author);
                });
    }


    private String getPostUrl(String key) {
        return String.format(JAVARUSH_API_FORMAT, key);
    }

    private Integer getAuthorId(PostInfo postInfo) {
        return postInfo.getAuthorInfo().getUserId();
    }

    private String getAuthorName(PostInfo postInfo) {
        return postInfo.getAuthorInfo().getDisplayName();
    }
}

