package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupStatDTO;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.StatisticDTO;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService{
    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public StatisticServiceImpl(GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public StatisticDTO countBotStatistic() {
        List<GroupStatDTO> groupStatDTOList = groupSubService.findAll().stream()
                .filter(it -> !isEmpty(it.getUsers()))
                .map(gs -> new GroupStatDTO(gs.getId(), gs.getTitle(), gs.getUsers().size()))
                .collect(Collectors.toList());
        List<TelegramUser> activeUsers = telegramUserService.findAllActiveUsers();
        Integer countInactiveUsers = telegramUserService.findAllInactiveUsers().size();
        return new StatisticDTO(activeUsers.size(), countInactiveUsers, getGroupsPerUser(activeUsers), groupStatDTOList);
    }

    private double getGroupsPerUser(List<TelegramUser> users) {
        return (double) users.stream().mapToInt(it -> it.getGroupSubs().size()).sum()/users.size();
    }
}
