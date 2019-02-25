package com.alexkbit.fakefacebot.config;

import com.alexkbit.fakefacebot.model.PhotoType;
import com.alexkbit.fakefacebot.model.Question;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("questions-config")
public class QuestionsConfig {

    private List<Question> questions;

    public Question findById(Integer id) {
        return questions.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean isValid(Integer id, PhotoType answer) {
        return findById(id).getType() == answer;
    }

    public Integer size() {
        return questions.size();
    }
}
