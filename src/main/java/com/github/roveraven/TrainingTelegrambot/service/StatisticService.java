package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;

/**
 * Service for getting bot statistics.
 */
public interface StatisticService {
    StatisticDTO countBotStatistic();
}
