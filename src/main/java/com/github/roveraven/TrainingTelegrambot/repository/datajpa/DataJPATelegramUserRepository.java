package com.github.roveraven.TrainingTelegrambot.repository.datajpa;

import com.github.roveraven.TrainingTelegrambot.repository.TelegramUserRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * {@link Repository} for handling with {@link TelegramUser} entity.
 */
//@Profile("datajpa")
    @Profile("!jdbc")
@Repository
public interface DataJPATelegramUserRepository extends JpaRepository<TelegramUser, Long>, TelegramUserRepository {

    List<TelegramUser> findAllByActiveTrue();

    List<TelegramUser> findAllByActiveFalse();
}
