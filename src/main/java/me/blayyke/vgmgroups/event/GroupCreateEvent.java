package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class GroupCreateEvent extends GropEvent {
    public GroupCreateEvent(Player source, Group group) {
        super(source, group);
    }

    @Override
    public Cause getCause() {
        return null;
    }
}