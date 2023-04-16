package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.command.TestUtils;
import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.AuthorInfo;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FindNewPostServiceTest {
    private GroupSubService groupSubService;
    private AuthorService authorService;
    private JavaRushPostClient javaRushPostClient;
    private SendBotMessageService sendBotMessageService;
    private FindNewPostService findNewPostService;

    @BeforeEach
    public void init() {
        groupSubService = Mockito.mock(GroupSubService.class);
        authorService = Mockito.mock(AuthorService.class);
        javaRushPostClient = Mockito.mock(JavaRushPostClient.class);
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        findNewPostService = new FindNewPostServiceImpl(groupSubService, authorService, javaRushPostClient, sendBotMessageService);
    }

    @Test
    public void shouldProperlyFindNewPosts() {
        //given
        GroupSub groupSub1 = TestUtils.getGroupSub(1, "g1");
        groupSub1.setLastPostId(500);
        TelegramUser user = TestUtils.getUser(133L, true, Collections.singletonList(groupSub1));
        groupSub1.setUsers(Collections.singletonList(user));
        List<GroupSub> listOfGroupSubs = new ArrayList<>();
        listOfGroupSubs.add(groupSub1);
        Mockito.when(groupSubService.findAll()).thenReturn(listOfGroupSubs);

        PostInfo post1 = new PostInfo();
        post1.setId(513);
        post1.setTitle("post1");
        post1.setDescription("First Post");
        post1.setKey("Link1");
        post1.setAuthorInfo(new AuthorInfo());
        PostInfo post2 = new PostInfo();
        post2.setId(613);
        post2.setTitle("post2");
        post2.setDescription("Second Post");
        post2.setKey("Link2");
        post2.setAuthorInfo(new AuthorInfo());
        List<PostInfo> postInfoList = new ArrayList<>();
        postInfoList.add(post1);
        postInfoList.add(post2);
        Mockito.when(javaRushPostClient.findNewPosts(groupSub1.getId(), groupSub1.getLastPostId()))
                .thenReturn(postInfoList);
        List<String> messages = new ArrayList<>();
        messages.add("""
                There is new post:\s
                 <b>post1</b>\s
                 
                In <b>group</b>:  <b>g1</b>
                From author: <b>null</b> with ID: <b>null</b>
                <b>Description:  </b>First Post\s


                <b>Reference: </b> https://javarush.com/api/1.0/rest/posts/Link1""");
        messages.add("""
                There is new post:\s
                 <b>post2</b>\s
                 
                In <b>group</b>:  <b>g1</b>
                From author: <b>null</b> with ID: <b>null</b>
                <b>Description:  </b>Second Post\s


                <b>Reference: </b> https://javarush.com/api/1.0/rest/posts/Link2""");
        Collections.reverse(messages);
        //when
        findNewPostService.findNewPosts();
        //then
        Assertions.assertEquals(post2.getId(), groupSub1.getLastPostId() );
        Mockito.verify(groupSubService).save(groupSub1);
        Mockito.verify(sendBotMessageService).sendMessage(user.getChatId(), messages);
    }

}