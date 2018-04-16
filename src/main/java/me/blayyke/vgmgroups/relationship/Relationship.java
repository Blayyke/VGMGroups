package me.blayyke.vgmgroups.relationship;

import java.util.Arrays;

public enum Relationship {
    ALLY("Truce"), ENEMY("Enemy"), TRUCE("Truce");

    private String friendlyName;

    Relationship(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static Relationship fromString(String relationship) {
        return Arrays.stream(values()).filter(s -> s.friendlyName.equalsIgnoreCase(relationship)).findFirst().get();
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}