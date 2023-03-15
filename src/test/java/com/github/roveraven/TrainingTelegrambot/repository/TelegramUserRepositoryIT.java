package com.github.roveraven.TrainingTelegrambot.repository;

import com.github.roveraven.TrainingTelegrambot.command.TestUtils;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
/**
 * Integration-level testing for {@link TelegramUserRepository}.
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace=NONE)
public class TelegramUserRepositoryIT {
    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/telegram_users.sql"})
    @Test
    public void shouldProperlyFindAllActiveUsers() {
        //when
        List<TelegramUser> users = telegramUserRepository.findAllByActiveTrue();

        //then
        Assertions.assertEquals(5, users.size());
    }

    @Sql(scripts = {"/sql/clearDbs.sql"})
    @Test
    public void shouldProperlySaveTelegramUser() {
        //when
        TelegramUser user = TestUtils.getUser(86538653787L, true, null);
        telegramUserRepository.save(user);

        //then
        Optional<TelegramUser> savedUser = telegramUserRepository.findById(user.getChatId());

        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertEquals(user, savedUser.get());

    }

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/FiveGroupSubsForUser.sql"})
    @Test
    public void shouldProperlyGetAllGroupSubsForUser() {
        //when
        Optional<TelegramUser> userFromDB = telegramUserRepository.findById(1L);
        //then
        Assertions.assertTrue(userFromDB.isPresent());
        List<GroupSub> groupSubs = userFromDB.get().getGroupSubs();
        for(int i=0; i< groupSubs.size(); i++) {
            Assertions.assertEquals(String.format("g%s", (i+1)), groupSubs.get(i).getTitle());
            Assertions.assertEquals(i+1, groupSubs.get(i).getId());
            Assertions.assertEquals(i+1, groupSubs.get(i).getLastPostId());
        }
    }

}
