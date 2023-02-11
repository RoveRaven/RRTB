package com.github.roveraven.TrainingTelegrambot.javarushclient.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Group discussion info class.
 */

@ToString(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupDiscussionInfo extends GroupInfo{
    private UserDiscussionInfo userDiscussionInfo;
    private Integer commentsCount;


}
