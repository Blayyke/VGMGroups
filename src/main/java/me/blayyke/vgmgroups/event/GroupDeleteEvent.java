package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class GroupDeleteEvent extends GroupEvent {
    public GroupDeleteEvent(Player owner, Group group, Cause cause) {
        super(owner, group, cause);
    }
}