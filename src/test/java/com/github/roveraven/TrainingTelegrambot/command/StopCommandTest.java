package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.roveraven.TrainingTelegrambot.command.CommandName.STOP;
import static com.github.roveraven.TrainingTelegrambot.command.StopCommand.STOP_MESSAGE;
import static com.github.roveraven.TrainingTelegrambot.command.TestUtils.*;

@DisplayName("Unit-level testing for StopCommand")
class StopCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return  STOP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return STOP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StopCommand(sendBotMessageService, telegramUserService);
    }

    @Test
    public void shouldSetExistingUserInActive() {
        //given
        TelegramUser user = getUser(1L, true, new ArrayList<>());
        Mockito.when(telegramUserService.findByChatId(1L)).thenReturn(Optional.of(user));
        Command command = new StopCommand(sendBotMessageService, telegramUserService);

        Update update = getUpdate(STOP.getCommandName(), 1L);
        //when
        command.execute(update);
        //then
        Mockito.verify(telegramUserService).save(user);
        Assertions.assertFalse(user.isActive());
    }
}