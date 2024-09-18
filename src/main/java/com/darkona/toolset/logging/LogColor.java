package com.darkona.toolset.logging;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LogColor {

    RESET("\u001B[0m"),
    BLACK("\u001B[0;30m"),
    DARK_BLUE("\u001B[0;34m"),
    DARK_GREEN("\u001B[0;32m"),
    DARK_AQUA("\u001B[0;36m"),
    DARK_RED("\u001B[0;31m"),
    DARK_PURPLE("\u001B[0;35m"),
    GOLD("\u001B[0;33m"),
    GRAY("\u001B[0;37m"),
    DARK_GRAY("\u001B[0;90m"),
    BLUE("\u001B[0;94m"),
    GREEN("\u001B[0;92m"),
    AQUA("\u001B[0;96m"),
    RED("\u001B[0;91m"),
    PURPLE("\u001B[0;95m"),
    PINK("\u001B[38;2;242;116;233m"),
    ORANGE("\u001B[38;2;252;132;39m"),
    LIGHT_PURPLE("\u001B[0;95m"),
    YELLOW("\u001B[0;93m"),
    WHITE("\u001B[0;97m");

    private final String ansi;

    public String get() {
        return ansi;
    }

    @Override
    public String toString() {
        return ansi;
    }
}
