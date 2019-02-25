package com.alexkbit.fakefacebot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class AppController {

    @Value("${telegram.bot.name}")
    private String botName;

    @GetMapping(value = "/info")
    @ResponseStatus(HttpStatus.OK)
    public Map getInfo() {
        log.info("getInfo() - start");
        Map<String, String> info = new HashMap<>();
        info.put("botName", botName);
        info.put("version", "1");
        log.info("getInfo() - end");
        return info;
    }
}
