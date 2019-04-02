package com.alexkbit.fakefacebot.controller;

import com.alexkbit.fakefacebot.bot.FakeFaceBot;
import com.alexkbit.fakefacebot.config.QuestionsConfig;
import com.alexkbit.fakefacebot.service.AccountService;
import com.alexkbit.fakefacebot.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {

    private static final String CONTENT_TYPES = "image/jpeg, image/jpg, image/png, image/gif";


    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${spring.application.version}")
    private String version;

    private final AccountService accountService;
    private final StatisticService statisticService;
    private final QuestionsConfig questionsConfig;
    private final FakeFaceBot fakeFaceBot;

    @GetMapping(value = "/info")
    @ResponseStatus(HttpStatus.OK)
    public Map getInfo() {
        log.debug("getInfo() - start");
        Map<String, String> info = new HashMap<>();
        info.put("botName", botName);
        info.put("version", version);
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
        stat.put("winnersCount", String.valueOf(questionsConfig.getWinnersCount()));
        log.debug("getStat() - end");
        return stat;
    }

    @GetMapping(value = "/top/incorrect")
    @ResponseStatus(HttpStatus.OK)
    public void getTopIncorrect(HttpServletResponse response) throws IOException {
        log.debug("getTopIncorrect() - start");
        InputStream is = statisticService.getTopIncorrectPhoto();
        log.debug("getTopIncorrect() - end");
        response.setContentType(CONTENT_TYPES);
        response.getOutputStream().write(IOUtils.toByteArray(is));
        response.getOutputStream().close();
    }

    @GetMapping(value = "/manage/notify")
    @ResponseStatus(HttpStatus.OK)
    public void getNotify() {
        log.debug("getNotify() - start");
        fakeFaceBot.notifyWinners();
        log.debug("getNotify() - end");
    }

    @GetMapping(value = "/manage/results")
    @ResponseStatus(HttpStatus.OK)
    public void getResults() {
        log.debug("getResults() - start");
        fakeFaceBot.sendResults();
        log.debug("getResults() - end");
    }

    @GetMapping(value = "/manage/enable")
    @ResponseStatus(HttpStatus.OK)
    public void enable() {
        log.debug("enable() - start");
        fakeFaceBot.enable();
        log.debug("enable() - end");
    }

    @GetMapping(value = "/manage/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disable() {
        log.debug("disable() - start");
        fakeFaceBot.disable();
        log.debug("disable() - end");
    }
}
