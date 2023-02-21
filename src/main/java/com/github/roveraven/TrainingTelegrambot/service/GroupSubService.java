package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;

/**
 * Service for manipulating with {@link GroupSub}.
 */
public interface GroupSubService {
    GroupSub save (String chatId, GroupDiscussionInfo groupDiscussionInfo);
    GroupSub save(GroupSub groupSub);
    Optional<GroupSub> findById(Integer groupId);
    List<GroupSub> findAll();

}
