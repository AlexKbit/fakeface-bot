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
                    sendKeyMessage(chatId, cmd.getKey(), account.getLocale(), questionsConfig.size());
                    return false;
                case CMD_HELP:
                    sendMessage(chatId, helpCommand(account.getLocale()));
                    return true;
                case CMD_SCORE:
                    sendScore(chatId, account);
                    return true;
                case CMD_TOP:
                    sendMessage(chatId, topCommand(account.getLocale()));
                    return true;
                case CMD_ID:
                    sendKeyMessage(chatId, cmd.getKey(), account.getLocale(), account.getAccountId().toString());
                    return true;
                case CMD_UNSUPPORTED:
                default:
                    sendKeyMessage(chatId, Command.CMD_UNSUPPORTED.getKey(), account.getLocale());
                    return true;
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
        if (!account.isFinished()) {
            return getMessage("messages.notFinished", account.getLocale());
        }
        Long score = accountService.getByAccountId(account.getAccountId()).getScore();
        Integer total = questionsConfig.size();
        long position = accountService.getPosition(account);
        Long allPeople = accountService.getTotal();
        Long allFinished = accountService.getTotalFinished();
        return getMessage(Command.CMD_SCORE.getKey(), account.getLocale(), score, total, position + 1, allPeople, allFinished);
    }

    private String topCommand(Locale locale) {
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
