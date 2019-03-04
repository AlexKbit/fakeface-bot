package com.alexkbit.fakefacebot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Data
@Document("accounts")
public class Account {
    @Id
    private String id;
    @Indexed
    private Integer accountId;
    private String login;
    private String firstName;
    private String lastName;
    private String langCode;
    private Long chatId;
    private Date createdAt = new Date();
    @Indexed
    private Long score;
    @Indexed
    private Date timestamp;
    private boolean finished = false;
    private List<Answer> answers = new ArrayList<>();

    public Locale getLocale() {
        if (langCode == null) {
            return Locale.ENGLISH;
        }
        return new Locale(langCode);
    }

    public void addAnswer(Integer id, PhotoType choose, Boolean valid) {
        answers.add(new Answer(id, choose, valid));
    }

    public Integer getCurrentQuestion() {
        return answers.stream().mapToInt(Answer::getQId).max().orElse(-1) + 1;
    }

    public void updateScore() {
        score = answers.stream().filter(Answer::getValid).count();
        timestamp = new Date();
        finished = true;
    }
}
