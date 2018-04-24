package me.blayyke.vgmgroups.enums;

import java.util.Arrays;

public enum Channel {
    ALLY("Ally"), TRUCE("Truce"), GROUP("Group");

    private String friendlyName;

    Channel(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static Channel fromString(String channel) {
        return Arrays.stream(values()).filter(s -> s.friendlyName.equalsIgnoreCase(channel)).findFirst().orElse(null);
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}