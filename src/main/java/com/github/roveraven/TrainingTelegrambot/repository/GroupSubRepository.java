package com.github.roveraven.TrainingTelegrambot.repository;

import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSubRepository extends JpaRepository<GroupSub, Integer> {
}
