package com.alexkbit.fakefacebot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Answer {
    @Id
    private ObjectId id;
    private Integer qId;
    private PhotoType choose;
    private Boolean valid;
}
