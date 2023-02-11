package com.github.roveraven.TrainingTelegrambot.javarushclient.dto;

import lombok.Data;
/**
 * DTO for User discussion info.
 */
@Data
public class UserDiscussionInfo {
    private Boolean isBookmarked;
    private Integer lastTime;
    private Integer newCommentsCount;
}
