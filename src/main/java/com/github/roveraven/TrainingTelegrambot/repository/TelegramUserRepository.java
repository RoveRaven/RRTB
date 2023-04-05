package com.github.roveraven.TrainingTelegrambot.repository;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;

import java.util.List;
import java.util.Optional;

public interface TelegramUserRepository {
    TelegramUser save(TelegramUser telegramUser);
    Optional<TelegramUser> findById(Long id);
    List<TelegramUser> findAllByActiveTrue();

    List<TelegramUser> findAllByActiveFalse();

}
