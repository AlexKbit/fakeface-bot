package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.config.QuestionsConfig;
import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Locale;

public abstract class CommandBot extends MessageBot {

    @Autowired
    protected QuestionsConfig questionsConfig;

    @Override
    protected boolean messageFilter(Message msg, Long chatId, Account account, String text) {
        if (msg.isCommand()) {
            Command cmd = Command.valueOfByText(msg.getText());
            switch (cmd) {
                case CMD_START:
                    sendKeyMessage(chatId, cmd.getKey(), account.getLocale());
                    return false;
                case CMD_UNSUPPORTED:
                    sendKeyMessage(chatId, cmd.getKey(), account.getLocale());
                    return true;
                case CMD_HELP:
                    sendMessage(chatId, helpCommand(account.getLocale()));
                    return true;
                case CMD_SCORE:
                    sendScore(chatId, account);
                    return true;
                case CMD_TOP:
                    sendMessage(chatId, topCommand(account.getAccountId(), account.getLocale()));
                    return true;
                case CMD_ID:
                    sendKeyMessage(chatId, cmd.getKey(), account.getLocale(), account.getAccountId());
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    protected void sendScore(Long chatId, Account account) {
        sendMessage(chatId, scoreCommand(account));
    }

    private String helpCommand(Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage(Command.CMD_HELP.getKey(), locale));
        for (Command cmd : Command.values()) {
            if (cmd != Command.CMD_UNSUPPORTED) {
                sb.append("\n");
                sb.append(String.format("%s - %s", cmd.getText(), getMessage(cmd.getDescription(), locale)));
            }
        }
        return sb.toString();
    }

    private String scoreCommand(Account account) {
        StringBuilder sb = new StringBuilder();
        Long score = accountService.getByAccountId(account.getAccountId()).getScore();
        Integer total = questionsConfig.size();
        Long position = accountService.getPosition(account.getScore(), account.getTimestamp());
        Long allPeople = accountService.getTotal();
        Long allFinished = accountService.getTotalFinished();
        return getMessage(Command.CMD_SCORE.getKey(), account.getLocale(), score, total, position + 1, allPeople, allFinished);
    }

    private String topCommand(Integer accountId, Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage(Command.CMD_TOP.getKey(), locale));
        List<Account> topList = accountService.getTop();
        for (int i=0; i< topList.size(); i++) {
            Account a = topList.get(i);
            sb.append(String.format("\n%s) [%s] %s %s = %s", i + 1, a.getLogin(), a.getFirstName(), a.getLastName(), a.getScore()));
        }
        return sb.toString();
    }
}
