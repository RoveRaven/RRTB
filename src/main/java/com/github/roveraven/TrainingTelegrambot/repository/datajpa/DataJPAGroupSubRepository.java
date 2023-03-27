package com.github.roveraven.TrainingTelegrambot.repository.datajpa;

import com.github.roveraven.TrainingTelegrambot.repository.GroupSubRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Profile("datajpa")
@Profile("!jdbc")
@Repository
public interface DataJPAGroupSubRepository extends GroupSubRepository, JpaRepository<GroupSub, Integer> {
}
