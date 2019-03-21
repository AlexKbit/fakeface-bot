package com.alexkbit.fakefacebot.model;

import lombok.Getter;

@Getter
public enum PhotoType {
    OTHER("classpath:photos/other/"),
    REAL("classpath:photos/real/"),
    FAKE("classpath:photos/fake/");

    private String path;

    PhotoType(String path) {
        this.path = path;
    }
}
