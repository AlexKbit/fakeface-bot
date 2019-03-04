package com.alexkbit.fakefacebot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection="answers")
public class Answer {
    @Id
    private String id;
    private Integer qId;
    private Integer accountId;
    private PhotoType choose;
    private Boolean valid;
}
