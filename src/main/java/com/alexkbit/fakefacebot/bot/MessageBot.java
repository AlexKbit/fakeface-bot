package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.config.QuestionsConfig;
import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.Answer;
import com.alexkbit.fakefacebot.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

@Slf4j
public abstract class MessageBot extends BaseBot {

    @Autowired
    protected QuestionsConfig questionsConfig;

    @Autowired
    private MessageService messageService;

    protected void sendKeyMessage(Long chatId, String key, Locale locale, Object... params) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(getMessage(key, locale, params));
        sendMessage(msg);
    }

    protected void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);
        sendMessage(msg);
    }

    protected String getMessage(String key, Locale locale, Object... params) {
        if (locale == null) {
            return messageService.getMessage(key, Locale.ENGLISH, params);
        }
        return messageService.getMessage(key, locale, params);
    }

    protected void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error send message", e);
        }
    }

    protected void sendResults(List<Account> accounts) {
        accounts.forEach(account -> sendMessage(account.getChatId(), resultTable(account)));
    }

    private String resultTable(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage("messages.youAnswers", account.getLocale()));
        sb.append("\n" + getMessage("messages.resultTableHead", account.getLocale()));
        for (Answer answer : account.getAnswers()) {
            String qId = answer.getQId() < 9 ? "  " + (answer.getQId() + 1) : String.valueOf(answer.getQId() + 1);
            String choose = answer.getChoose().name();
            String valid = answer.getValid() ? "✅" : "❌";
            sb.append("\n" + qId + ")\t" + choose + "\t\t" + valid);
        }
        return sb.toString();
    }

}
