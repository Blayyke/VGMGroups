package me.blayyke.vgmgroups.enums;

import java.util.Arrays;

public enum Rank {
    OFFICER("Officer", "*"), MEMBER("Member", "+"), RECRUIT("Recruit", "-");

    private final String friendlyName;
    private final String chatPrefix;

    Rank(String friendlyName, String chatPrefix) {
        this.friendlyName = friendlyName;
        this.chatPrefix = chatPrefix;
    }

    public static Rank fromString(String rank) {
        return Arrays.stream(values()).filter(s -> s.friendlyName.equalsIgnoreCase(rank)).findFirst().orElse(null);
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }
}