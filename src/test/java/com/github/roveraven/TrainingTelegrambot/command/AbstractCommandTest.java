package com.github.roveraven.TrainingTelegrambot.command;

import com.github.roveraven.TrainingTelegrambot.bot.RRTelegramBot;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageService;
import com.github.roveraven.TrainingTelegrambot.service.SendBotMessageServiceImpl;
import com.github.roveraven.TrainingTelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
/**
 * Abstract class for testing {@link Command}s.
 */
abstract class AbstractCommandTest {
protected RRTelegramBot rrTelegramBot = Mockito.mock(RRTelegramBot.class);
protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(rrTelegramBot);

abstract String getCommandName();
abstract String getCommandMessage();
abstract Command getCommand();

@Test
public void shouldProperlyExecuteCommand() throws TelegramApiException {
    //given
    Long chatId = 1234567824356L;
    Update update = TestUtils.getUpdate(getCommandName(), chatId);

    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId.toString());
    sendMessage.setText(getCommandMessage());
    sendMessage.enableHtml(true);
    //when
    getCommand().execute(update);
    //then
    Mockito.verify(rrTelegramBot).execute(sendMessage);


}


}