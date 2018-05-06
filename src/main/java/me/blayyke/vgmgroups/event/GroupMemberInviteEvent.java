package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class GroupMemberInviteEvent extends GroupEvent {
    private final Player invited;

    public GroupMemberInviteEvent(Player source, Group group, Cause cause, Player invited) {
        super(source, group, cause);
        this.invited = invited;
    }

    public Player getInvited() {
        return invited;
    }
}