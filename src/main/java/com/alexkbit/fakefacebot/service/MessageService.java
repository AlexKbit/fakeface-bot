package com.alexkbit.fakefacebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    public String getMessage(String key, Locale locale, Object... params) {
        return messageSource.getMessage(key, params, locale);
    }
}
