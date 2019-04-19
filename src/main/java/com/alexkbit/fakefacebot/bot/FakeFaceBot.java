package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.PhotoType;
import com.alexkbit.fakefacebot.model.Question;
import com.alexkbit.fakefacebot.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FakeFaceBot extends CommandBot {

    private static final String FAKE = "FAKE";
    private static final String REAL = "REAL";

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private PhotoService photoService;

    private volatile boolean enable = true;

    @Override
    public void onReceived(Message msg, Long chatId, Account account, String text) {
        if (account.isFinished()) {
            sendKeyMessage(chatId, "messages.done", account.getLocale());
            return;
        }
        if (!enable) {
            sendKeyMessage(chatId, "messages.end", account.getLocale());
            return;
        }
        Integer currentQuestion = account.getCurrentQuestion();
        switch (text.toUpperCase()) {
            case FAKE:
                account.addAnswer(currentQuestion, PhotoType.FAKE, questionsConfig.isValid(currentQuestion, PhotoType.FAKE));
                accountService.update(account);
                break;
            case REAL:
                account.addAnswer(currentQuestion, PhotoType.REAL, questionsConfig.isValid(currentQuestion, PhotoType.REAL));
                accountService.update(account);
                break;
        }
        sendNextQuestion(chatId, account);
    }

    private void sendNextQuestion(Long chatId, Account account) {
        Integer nextQuestion = account.getCurrentQuestion();
        Integer totalQuestion = questionsConfig.size();
        if (nextQuestion >= totalQuestion) {
            account.updateScore();
            accountService.update(account);
            sendKeyMessage(chatId, "messages.done", account.getLocale());
            sendScore(chatId, account);
            log.debug("Account = {} completed quiz", account);
            return;
        }
        sendNextPhoto(chatId, account, nextQuestion);
    }

    private void sendNextPhoto(Long chatId, Account account, Integer nextPhoto) {
        Question q = questionsConfig.findById(nextPhoto);
        SendPhoto photo = photoService.getPhoto(q.getPhoto(), q.getType());
        photo.setCaption(getMessage("messages.chooseFace", account.getLocale(), nextPhoto + 1));
        photo.setChatId(chatId);
        photo.setReplyMarkup(createKeyBoard());
        try {
            Message response = execute(photo);
            if (!response.getPhoto().isEmpty()) {
                photoService.updateCache(q.getPhoto(), q.getType(), response.getPhoto().get(0).getFileId());
            }
        } catch (TelegramApiException e) {
            log.error("Send photo to chatId = {} error: ", e, chatId);
        }
    }

    private ReplyKeyboardMarkup createKeyBoard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(FAKE));
        keyboardFirstRow.add(new KeyboardButton(REAL));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public void notifyWinners() {
        if (enable) {
            return;
        }
        long wCount = questionsConfig.getWinnersCount();
        List<Account> winners = accountService.getTop().stream().limit(wCount).collect(Collectors.toList());
        winners.forEach(account -> sendKeyMessage(account.getChatId(), "messages.winner", account.getLocale()));
        log.info("Notify winners: {}", winners);
    }

    public void sendResults() {
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

    public void enable() {
        this.enable = true;
    }

    public void disable() {
        this.enable = false;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
