package com.github.roveraven.TrainingTelegrambot.javarushclient.dto;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushGroupClient;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Implementation of the {@link JavaRushGroupClient} interface.
 */
@Component
public class JavaRushGroupClientImpl implements JavaRushGroupClient {
    private final String javaRushApiGroupPath;

    public JavaRushGroupClientImpl(@Value("${javarush.api.path}") String javaRushApi) {
        this.javaRushApiGroupPath = javaRushApi + "/groups";
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
}
