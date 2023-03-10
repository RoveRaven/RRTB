package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JavaRushPostClientImpl implements JavaRushPostClient {
    private final String javaRushApiPostPath;

    public JavaRushPostClientImpl(@Value("${javarush.api.path}") String javaRushApi) {
        this.javaRushApiPostPath = javaRushApi + "/posts";
    }

    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        List<PostInfo> lastPostsOgGroup = Unirest.get(javaRushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 15)
                .asObject(new GenericType<List<PostInfo>>(){})
                .getBody();
        List<PostInfo> newPosts = new ArrayList<>();
        for (PostInfo post : lastPostsOgGroup) {
            if (lastPostId.equals(post.getId())) {
                return newPosts;
            }
            newPosts.add(post);
        }
        return newPosts;
    }
}
