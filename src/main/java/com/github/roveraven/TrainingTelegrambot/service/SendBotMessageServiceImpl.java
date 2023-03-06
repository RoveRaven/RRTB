package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.bot.RRTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
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
            //todo add logging to the project.
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Long chatId, List<String> messages) {
        if(messages.isEmpty()) return;
        messages.forEach(m->sendMessage(chatId, m));
    }
}
