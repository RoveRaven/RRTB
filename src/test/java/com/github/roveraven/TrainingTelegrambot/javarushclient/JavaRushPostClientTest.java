package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaRushPostClientTest {

    @Test
    public void shouldProperlyGet15NewPosts() {
        JavaRushPostClientImpl postClient = new JavaRushPostClientImpl("https://javarush.com/api/1.0/rest");

        List<PostInfo> newPosts = postClient.findNewPosts(16, 2935);
        Assertions.assertEquals(15, newPosts.size());
    }
}