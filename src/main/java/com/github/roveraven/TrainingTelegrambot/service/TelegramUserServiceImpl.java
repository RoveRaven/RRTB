package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.repository.TelegramUserRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of {@link TelegramUserService}.
 */
@Service
public class TelegramUserServiceImpl implements TelegramUserService{
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository=telegramUserRepository;
    }
    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public List<TelegramUser> findAllActiveUsers() {
        return telegramUserRepository.findAllByActiveTrue();
    }

    @Override
    public List<TelegramUser> findAllInactiveUsers() {
        return telegramUserRepository.findAllByActiveFalse();
    }

    @Override
    public Optional<TelegramUser> findByChatId(Long chatId) {
        return telegramUserRepository.findById(chatId);
    }
}
