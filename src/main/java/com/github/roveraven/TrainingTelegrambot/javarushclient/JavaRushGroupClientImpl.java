package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.*;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Implementation of the {@link JavaRushGroupClient} interface.
 */
@Component
public class JavaRushGroupClientImpl implements JavaRushGroupClient {
    private final String javaRushApiGroupPath;
    private final String javaRushApiPostPath;

    public JavaRushGroupClientImpl(@Value("${javarush.api.path}") String javaRushApi) {
        this.javaRushApiGroupPath = javaRushApi + "/groups";
        this.javaRushApiPostPath = javaRushApi + "/posts";
    }

    @Override
    public List<GroupInfo> getGroupArgs(GroupRequestsArgs groupRequestsArgs) {
        return Unirest.get(javaRushApiGroupPath)
                .queryString(groupRequestsArgs.populateQueries())
                .asObject(new GenericType<List<GroupInfo>>() {
                })
                .getBody();
    }
    

    @Override
    public List<GroupDiscussionInfo> getGroupDiscussionArgs(GroupRequestsArgs groupRequestsArgs) {
        return Unirest.get(javaRushApiGroupPath)
                .queryString(groupRequestsArgs.populateQueries())
                .asObject(new GenericType<List<GroupDiscussionInfo>>() {
                })
                .getBody();
    }

    @Override
    public Integer getGroupCount(GroupCountRequestArgs groupCountRequestArgs) {
        return Integer.valueOf(Unirest.get(String.format("%s/count", javaRushApiGroupPath))
                .queryString(groupCountRequestArgs.populateQueries())
                .asString()
                .getBody());
    }

    @Override
    public GroupDiscussionInfo getGroupById(Integer id) {
        return Unirest.get(String.format("%s/group%s", javaRushApiGroupPath, id.toString()))
                .asObject(GroupDiscussionInfo.class)
                .getBody();
    }
    public Integer getLastArticleId(Integer groupSubId){
        List<PostInfo> posts = Unirest.get(javaRushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupSubId.toString())
                .queryString("limit", "1")
                .asObject(new GenericType<List<PostInfo>>() {
                })
                .getBody();
        return isEmpty(posts)?0: Optional.ofNullable(posts.get(0)).map(PostInfo::getId).orElse(0);
    }
}
