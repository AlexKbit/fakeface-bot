package com.alexkbit.fakefacebot.controller;

import com.alexkbit.fakefacebot.bot.FakeFaceBot;
import com.alexkbit.fakefacebot.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private FakeFaceBot fakeFaceBot;

    @GetMapping(value = "/info")
    @ResponseStatus(HttpStatus.OK)
    public Map getInfo() {
        log.debug("getInfo() - start");
        Map<String, String> info = new HashMap<>();
        info.put("botName", botName);
        info.put("version", "1.0.0");
        log.debug("getInfo() - end");
        return info;
    }

    @GetMapping(value = "/stat")
    @ResponseStatus(HttpStatus.OK)
    public Map getStat() {
        log.debug("getStat() - start");
        Map<String, Object> stat = new HashMap<>();
        stat.put("top", accountService.getTop());
        stat.put("totalFinished", accountService.getTotalFinished());
        stat.put("totalTotal", accountService.getTotal());
        log.debug("getStat() - end");
        return stat;
    }

    @GetMapping(value = "/notify")
    @ResponseStatus(HttpStatus.OK)
    public void getNotify() {
        log.debug("getNotify() - start");
        fakeFaceBot.notifyWinners();
        log.debug("getNotify() - end");
    }

    @GetMapping(value = "/enable")
    @ResponseStatus(HttpStatus.OK)
    public void enable() {
        log.debug("enable() - start");
        fakeFaceBot.enable();
        log.debug("enable() - end");
    }

    @GetMapping(value = "/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disable() {
        log.debug("disable() - start");
        fakeFaceBot.disable();
        log.debug("disable() - end");
    }
}
