package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupCountRequestArgs;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupInfo;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupRequestsArgs;

import java.util.List;

/**
 * Client for Javarush Open API corresponds to Groups.
 */
public interface JavaRushGroupClient {
    /**
     * Get all the {@link GroupInfo} filtered by provided {@link GroupRequestsArgs}.
     *
     * @param groupRequestsArgs provided {@link GroupRequestsArgs}.
     * @return the collection of the {@link GroupInfo} objects.
     */
    List<GroupInfo> getGroupArgs (GroupRequestsArgs groupRequestsArgs);
    /**
     * Get all the {@link GroupDiscussionInfo} filtered by provided {@link GroupRequestsArgs}.
     *
     * @param groupRequestsArgs provided {@link GroupRequestsArgs}
     * @return the collection of the {@link GroupDiscussionInfo} objects.
     */
    List<GroupDiscussionInfo> getGroupDiscussionArgs(GroupRequestsArgs groupRequestsArgs);
    /**
     * Get count of groups filtered by provided {@link GroupRequestsArgs}.
     *
     * @param groupCountRequestArgs provided {@link GroupCountRequestArgs}.
     * @return the count of the groups.
     */
    Integer getGroupCount (GroupCountRequestArgs groupCountRequestArgs);
    /**
     * Get {@link GroupDiscussionInfo} by provided ID.
     *
     * @param id provided ID.
     * @return {@link GroupDiscussionInfo} object.
     */
    GroupDiscussionInfo getGroupById(Integer id);

    public Integer getLastArticleId(Integer groupSubId);



}
