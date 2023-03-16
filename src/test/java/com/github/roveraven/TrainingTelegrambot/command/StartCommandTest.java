package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.START;
import static com.github.roveraven.TrainingTelegrambot.command.StartCommand.START_MESSAGE;
import static com.github.roveraven.TrainingTelegrambot.command.TestUtils.getUpdate;
import static com.github.roveraven.TrainingTelegrambot.command.TestUtils.getUser;
@DisplayName("Unit-level testing for StartCommand")
class StartCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return  START.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return START_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StartCommand(sendBotMessageService, telegramUserService);
    }

    @Test
    public void shouldSetExistingUserActive() {
        //given
        TelegramUser user = getUser(1L, false, new ArrayList<>());
        Mockito.when(telegramUserService.findByChatId(1L)).thenReturn(Optional.of(user));
        Command command = new StartCommand(sendBotMessageService, telegramUserService);

        Update update = getUpdate(START.getCommandName(), 1L);
        //when
        command.execute(update);
        //then
        Mockito.verify(telegramUserService).save(user);
        Assertions.assertTrue(user.isActive());
    }
}