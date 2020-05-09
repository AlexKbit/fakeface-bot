package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.AdminCommand;
import com.alexkbit.fakefacebot.model.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CommandBot extends MessageBot {

    private volatile boolean enable = true;

    @Override
    protected boolean messageFilter(Message msg, Long chatId, Account account, String text) {
        if (msg.isCommand()) {
            return executeAdminCommands(msg, chatId, account) || executeUserCommands(msg, chatId, account);
        }
        return false;
    }

    private Boolean executeAdminCommands(Message msg, Long chatId, Account account) {
        if (getAdminAccountId().equals(account.getAccountId())) {
            Optional<AdminCommand> cmd = AdminCommand.valueOfByText(msg.getText());
            if (cmd.isPresent()) {
                AdminCommand adminCmd = cmd.get();
                switch (adminCmd) {
                    case CMD_ADMIN:
                        sendAdminMessage(chatId);
                        return true;
                    case CMD_ACTIVATE:
                        enablePlay();
                        sendMessage(chatId, "ADMIN: enabled");
                        return true;
                    case CMD_DEACTIVATE:
                        disablePlay();
                        sendMessage(chatId, "ADMIN: disabled");
                        return true;
                    case CMD_NOTIFY:
                        notifyWinners();
                        sendMessage(chatId, "ADMIN: players notified");
                        return true;
                    case CMD_SEND_RESULTS:
                        sendResults();
                        sendMessage(chatId, "ADMIN: result generated");
                        return true;
                    default:
                        sendKeyMessage(chatId, Command.CMD_UNSUPPORTED.getKey(), account.getLocale());
                        return true;
                }
            }
        }
        return false;
    }

    private Boolean executeUserCommands(Message msg, Long chatId, Account account) {
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
            case CMD_INFO:
                sendKeyMessage(chatId, cmd.getKey(), account.getLocale());
                return true;
            case CMD_UNSUPPORTED:
            default:
                sendKeyMessage(chatId, Command.CMD_UNSUPPORTED.getKey(), account.getLocale());
                return true;
        }
    }

    protected void sendScore(Long chatId, Account account) {
        sendMessage(chatId, scoreCommand(account));
    }

    private void sendAdminMessage(Long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("ADMIN MODE");
        msg.setReplyMarkup(createAdminKeyBoard());
        sendMessage(msg);
    }

    private ReplyKeyboardMarkup createAdminKeyBoard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(AdminCommand.CMD_ACTIVATE.getText()));
        keyboardFirstRow.add(new KeyboardButton(AdminCommand.CMD_DEACTIVATE.getText()));
        keyboardFirstRow.add(new KeyboardButton(AdminCommand.CMD_NOTIFY.getText()));
        keyboardFirstRow.add(new KeyboardButton(AdminCommand.CMD_SEND_RESULTS.getText()));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
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
        Long winnersCount = questionsConfig.getWinnersCount();
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage(Command.CMD_TOP.getKey(), locale));
        List<Account> topList = accountService.getTop();
        for (int i=0; i< topList.size(); i++) {
            Account a = topList.get(i);
            String isWinner = i < winnersCount ? "\uD83C\uDFC6" : "";
            sb.append(String.format("\n%s) %s[%s] %s %s = %s", i + 1, isWinner, a.getLogin(), a.getFirstName(), a.getLastName(), a.getScore()));
        }
        return sb.toString();
    }

    private void notifyWinners() {
        if (enable) {
            return;
        }
        long wCount = questionsConfig.getWinnersCount();
        List<Account> winners = accountService.getTop().stream().limit(wCount).collect(Collectors.toList());
        winners.forEach(account -> sendKeyMessage(account.getChatId(), "messages.winner", account.getLocale()));
    }

    private void sendResults() {
        if (enable) {
            return;
        }
        Page<Account> page = accountService.getPage(0, 20);
        sendResults(page.getContent());
        while (!page.isLast()) {
            Pageable p = page.nextPageable();
            page = accountService.getPage(p.getPageNumber(), p.getPageSize());
            sendResults(page.getContent());
        }
    }

    private void enablePlay() {
        this.enable = true;
    }

    private void disablePlay() {
        this.enable = false;
    }

    protected Boolean getEnable() {
        return this.enable;
    }

    protected abstract Integer getAdminAccountId();
}
