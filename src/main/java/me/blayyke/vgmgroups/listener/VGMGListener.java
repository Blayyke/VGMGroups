package me.blayyke.vgmgroups.listener;

import me.blayyke.vgmgroups.VGMGroups;

class VGMGListener {
    VGMGListener() {
        VGMGroups.getLogger().info("Registered listener " + getClass().getSimpleName() + ".");
    }
}