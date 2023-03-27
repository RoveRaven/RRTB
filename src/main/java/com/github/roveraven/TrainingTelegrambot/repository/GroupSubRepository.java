package com.github.roveraven.TrainingTelegrambot.repository;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;


public interface GroupSubRepository {
    Optional<GroupSub> findById(Integer id);

    GroupSub save(GroupSub groupSub);
    List<GroupSub> findAll();
}
