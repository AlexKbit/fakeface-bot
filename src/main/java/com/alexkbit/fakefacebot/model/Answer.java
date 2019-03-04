package com.alexkbit.fakefacebot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Answer {
    private Integer qId;
    private PhotoType choose;
    private Boolean valid;
}
