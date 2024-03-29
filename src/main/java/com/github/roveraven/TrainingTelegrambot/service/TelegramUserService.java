package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * {@link Service} for handling {@link TelegramUser} entity.
 */
public interface TelegramUserService {
    /**
     * Save provided {@link TelegramUser} entity.
     *
     * @param  telegramUser provided telegram user.
     */
    void save(TelegramUser telegramUser);
    /**
     * Find all active {@link TelegramUser}.
     *
     * @return the collection of the active {@link TelegramUser} objects.
     */
    List<TelegramUser> findAllActiveUsers();

    List<TelegramUser> findAllInactiveUsers();
    /**
     * Find {@link TelegramUser} by chatId.
     *
     * @param chatId provided Chat ID
     * @return {@link TelegramUser} with provided chat ID or null otherwise.
     */
    Optional<TelegramUser> findByChatId(Long chatId);
}
