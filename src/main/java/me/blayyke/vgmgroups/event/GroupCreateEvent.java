package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class GroupCreateEvent extends GroupEvent {
    public GroupCreateEvent(Player source, Group group, Cause cause) {
        super(source, group, cause);
    }
}