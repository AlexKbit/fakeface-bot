package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Slf4j
public abstract class MessageBot extends BaseBot {

    @Autowired
    private MessageService messageService;

    public void sendKeyMessage(Long chatId, String key, Locale locale, Object... params) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(getMessage(key, locale, params));
        sendMessage(msg);
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);
        sendMessage(msg);
    }

    public String getMessage(String key, Locale locale, Object... params) {
        return messageService.getMessage(key, locale, params);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error send message", e);
        }
    }
}
