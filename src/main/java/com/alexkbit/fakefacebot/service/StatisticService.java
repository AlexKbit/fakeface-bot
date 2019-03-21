package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.config.QuestionsConfig;
import com.alexkbit.fakefacebot.model.AnswerCount;
import com.alexkbit.fakefacebot.model.PhotoType;
import com.alexkbit.fakefacebot.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private static final String DEFAULT_IMAGE = "question";

    private final AccountService accountService;
    private final QuestionsConfig questionsConfig;
    private final PhotoService photoService;

    public InputStream getTopIncorrectPhoto() {
        List<AnswerCount> res = accountService.getTopIncorrectAnswer();
        if (res.isEmpty()) {
            return photoService.loadPhoto(DEFAULT_IMAGE, PhotoType.OTHER);
        }
        Integer qId = res.get(0).getQId();
        Question question = questionsConfig.findById(qId);
        return photoService.loadPhoto(question.getPhoto(), question.getType());
    }
}
