package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
public abstract class BaseBot extends TelegramLongPollingBot {

    @Autowired
    protected AccountService accountService;

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if (msg == null) {
            return;
        }
        User user = msg.getFrom();
        String text = msg.getText();
        Long chatId = update.getMessage().getChatId();
        Account account = findAccount(msg);
        if (messageFilter(msg, chatId, account, text)) {
            return;
        }
        onReceived(msg, chatId, account, text);
    }

    protected abstract void onReceived(Message msg, Long chatId, Account account, String text);

    protected abstract boolean messageFilter(Message msg, Long chatId, Account account, String text);

    private Account findAccount(Message msg) {
        User user = msg.getFrom();
        Long chatId = msg.getChatId();
        Account account = accountService.getByAccountId(user.getId());
        if (account != null) {
            log.debug("Load account: {}", account);
            return account;
        }
        account = accountService.registration(
                user.getId(), user.getUserName(),
                user.getFirstName(), user.getLastName(),
                user.getLanguageCode(), chatId);
        account.setChatId(chatId);
        return account;
    }
}
