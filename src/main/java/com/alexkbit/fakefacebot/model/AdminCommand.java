package com.alexkbit.fakefacebot.model;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum AdminCommand {

    CMD_ADMIN("admin"),
    CMD_ACTIVATE("activate"),
    CMD_DEACTIVATE("deactivate"),
    CMD_NOTIFY("notify"),
    CMD_SEND_RESULTS("results");

    private String name;
    private String text;

    AdminCommand(String name) {
        this.name = name;
        this.text = "/" + name;
    }

    public boolean isCurrent(String text) {
        return this.getText().equalsIgnoreCase(text);
    }

    public static Optional<AdminCommand> valueOfByText(String text) {
        return Stream.of(AdminCommand.values()).filter(cmd -> cmd.isCurrent(text)).findFirst();
    }
}
