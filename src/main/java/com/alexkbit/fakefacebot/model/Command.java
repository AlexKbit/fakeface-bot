package com.alexkbit.fakefacebot.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Command {

    CMD_ID("id", "messages.accountId", "messages.description.cmd.accountId"),
    CMD_UNSUPPORTED("", "messages.description.cmd.unsupported", "messages.description.cmd.unsupported"),
    CMD_START("start", "messages.welcome", "messages.description.cmd.start"),
    CMD_TOP("top", "messages.topTitle", "messages.description.cmd.top"),
    CMD_SCORE("score", "messages.scoreTitle", "messages.description.cmd.score"),
    CMD_INFO("info", "messages.infoTitle", "messages.description.cmd.info"),
    CMD_HELP("help", "messages.helpTitle", "messages.description.cmd.help");

    private String name;
    private String text;
    private String key;
    private String description;

    Command(String name, String key, String description) {
        this.name = name;
        this.text = "/" + name;
        this.key = key;
        this.description = description;
    }

    public boolean isCurrent(String text) {
        return this.getText().equalsIgnoreCase(text);
    }

    public static Command valueOfByText(String text) {
        return Stream.of(Command.values()).filter(cmd -> cmd.isCurrent(text)).findFirst().orElse(CMD_UNSUPPORTED);
    }
}
