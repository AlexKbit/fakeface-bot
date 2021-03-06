package com.alexkbit.fakefacebot.bot;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.PhotoType;
import com.alexkbit.fakefacebot.model.Question;
import com.alexkbit.fakefacebot.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FakeFaceBot extends CommandBot {

    private static final String FAKE = "FAKE";
    private static final String REAL = "REAL";

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${admin.accountId}")
    private Integer adminAccountId;

    @Autowired
    private PhotoService photoService;


    @Override
    public void onReceived(Message msg, Long chatId, Account account, String text) {
        if (account.isFinished()) {
            sendKeyMessage(chatId, "messages.done", account.getLocale());
            return;
        }
        if (!getEnable()) {
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
        sendNextPhoto(chatId, account, nextQuestion, totalQuestion);
    }

    private void sendNextPhoto(Long chatId, Account account, Integer nextPhoto, Integer total) {
        Question q = questionsConfig.findById(nextPhoto);
        SendPhoto photo = photoService.getPhoto(q.getPhoto(), q.getType());
        photo.setCaption(getMessage("messages.chooseFace", account.getLocale(), nextPhoto + 1, total));
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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    protected Integer getAdminAccountId() {
        return adminAccountId;
    }
}
