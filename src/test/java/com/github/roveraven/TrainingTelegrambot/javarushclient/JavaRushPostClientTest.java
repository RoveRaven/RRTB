package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
@DisplayName("Integration-level testing for JavaRushPostClientImplTest")
class JavaRushPostClientTest {

    @Test
    public void shouldProperlyGet15NewPosts() {
        JavaRushPostClient postClient = new JavaRushPostClientImpl("https://javarush.com/api/1.0/rest");

        List<PostInfo> newPosts = postClient.findNewPosts(16, 2935);
        Assertions.assertEquals(15, newPosts.size());
    }
}