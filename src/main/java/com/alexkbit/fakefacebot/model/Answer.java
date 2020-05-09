package com.alexkbit.fakefacebot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("answers")
@AllArgsConstructor
public class Answer {
    @Id
    private ObjectId id;
    private Integer qId;
    private PhotoType choose;
    private Boolean valid;
}
