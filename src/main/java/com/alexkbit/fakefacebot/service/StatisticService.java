package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.config.QuestionsConfig;
import com.alexkbit.fakefacebot.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final AccountService accountService;
    private final QuestionsConfig questionsConfig;
    private final PhotoService photoService;

    public InputStream getTopIncorrectPhoto() {
        Integer qId = accountService.getTopIncorrectAnswer().get(0).getQId();
        Question question = questionsConfig.findById(qId);
        return photoService.loadPhoto(question.getPhoto(), question.getType());
    }
}
