package com.gpslab.kaun.block;

public enum BlockedNumberType {
    EXACT_MATCH("Exact match"),
    REGEX_MATCH("Prefix match");

    BlockedNumberType(final String displayText) {
        this.displayText = displayText;
    }

    private final String displayText;

    public String getDisplayText() {
        return displayText;
    }
}
