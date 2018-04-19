package me.blayyke.vgmgroups.enums;

import java.util.Arrays;

public enum Relationship {
    ALLY("Ally"), ENEMY("Enemy"), TRUCE("Truce"), NEUTRAL("Neutral");

    private String friendlyName;

    Relationship(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static Relationship fromString(String relationship) {
        return Arrays.stream(values()).filter(s -> s.friendlyName.equalsIgnoreCase(relationship)).findFirst().orElse(null);
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}