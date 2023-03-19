package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.bot.RRTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Slf4j(topic = "SendBotMessageService")
@Service
public class SendBotMessageServiceImpl implements  SendBotMessageService{

    private final RRTelegramBot rrTelegramBot;
    @Autowired
    public SendBotMessageServiceImpl(RRTelegramBot rrTelegramBot) {
        this.rrTelegramBot = rrTelegramBot;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            rrTelegramBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {
            log.error("TelegramException while trying to send message", e);
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Long chatId, List<String> messages) {
        if(messages.isEmpty()) return;
        messages.forEach(m->sendMessage(chatId, m));
    }
}
