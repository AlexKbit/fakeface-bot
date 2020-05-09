package com.alexkbit.fakefacebot.model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String photo;
    private PhotoType type;
}
