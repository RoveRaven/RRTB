package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.bot.RRTelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@DisplayName("Unit-level testing for SendBotMessageServiceTest")
class SendBotMessageServiceTest {
private SendBotMessageService sendBotMessageService;
private RRTelegramBot rrTelegramBot;
@BeforeEach
public void init() {
    rrTelegramBot = Mockito.mock(RRTelegramBot.class);
    sendBotMessageService = new SendBotMessageServiceImpl(rrTelegramBot);
}
@Test
public void ShouldProperlySendMessage() throws TelegramApiException {
    //given
    Long chatId = 1347567L;
    String message = "test_message";

    SendMessage sendMessage = new SendMessage();
    sendMessage.setText(message);
    sendMessage.setChatId(chatId);
    sendMessage.enableHtml(true);
    //when
    sendBotMessageService.sendMessage(chatId, message);
    //then
    Mockito.verify(rrTelegramBot).execute(sendMessage);
}

}