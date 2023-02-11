package com.github.roveraven.TrainingTelegrambot.javarushclient;

import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static com.github.roveraven.TrainingTelegrambot.javarushclient.dto.GroupInfoType.TECH;

@DisplayName("Integration-level testing for JavaRushGroupClientImplTest")
public class JavaRushGroupClientTest {
    private final JavaRushGroupClient groupClient = new JavaRushGroupClientImpl("https://javarush.com/api/1.0/rest");

    @Test
    public void shouldProperlyGetGroupsWithEmptyArgs() {
        //given
        GroupRequestsArgs groupRequestsArgs = GroupRequestsArgs.builder().build();
        //when
        List<GroupInfo> groupList = groupClient.getGroupArgs(groupRequestsArgs);
        //then
        Assertions.assertNotNull(groupList);
        Assertions.assertFalse(groupList.isEmpty());
    }

    @Test
    public void ShouldProperlyGetWithOffsetAndLimit() {
        //given
        GroupRequestsArgs groupRequestsArgs = GroupRequestsArgs.builder()
                .offset(1)
                .limit(3)
                .build();
        //when
        List<GroupInfo> groupList = groupClient.getGroupArgs(groupRequestsArgs);
        //then
        Assertions.assertFalse(groupList.isEmpty());
        Assertions.assertEquals(3, groupList.size());

    }

    @Test
    public void shouldProperlyGetGroupDiscWithEmptyArgs() {
        //given
        GroupRequestsArgs groupRequestsArgs = GroupRequestsArgs.builder().build();
        //when
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionArgs(groupRequestsArgs);
        //then
        Assertions.assertNotNull(groupList);
        Assertions.assertFalse(groupList.isEmpty());
    }

    @Test
    public void ShouldProperlyGetGroupDiscWithOffsetAndLimit() {
        //given
        GroupRequestsArgs groupRequestsArgs = GroupRequestsArgs.builder()
                .offset(1)
                .limit(3)
                .build();
        //when
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionArgs(groupRequestsArgs);
        //then
        Assertions.assertFalse(groupList.isEmpty());
        Assertions.assertEquals(3, groupList.size());

    }

    @Test
    public void shouldProperlyGetGroupCount() {
        //given
        GroupCountRequestArgs groupCountRequestArgs = GroupCountRequestArgs.builder().build();
        //when
        Integer groupCount = groupClient.getGroupCount(groupCountRequestArgs);
        //then
        Assertions.assertEquals(32, groupCount);
    }

    @Test
    public void shouldProperlyGetGroupTECHCount(){
        //given
        GroupCountRequestArgs groupCountRequestArgs = GroupCountRequestArgs.builder()
                .type(TECH)
                .build();
        //when
        Integer groupCount = groupClient.getGroupCount(groupCountRequestArgs);
        //then
        Assertions.assertEquals(7, groupCount);
    }
    @Test
    public void shouldProperlyGetGroupById() {
        //given
        Integer androidGroupId = 16;
        //when
        GroupDiscussionInfo groupById = groupClient.getGroupById(androidGroupId);
        //then
        Assertions.assertNotNull(groupById);
        Assertions.assertEquals(16, groupById.getId());
        Assertions.assertEquals(TECH, groupById.getType());
        Assertions.assertEquals("android", groupById.getKey());
    }
}
