package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

public abstract class GroupEvent extends AbstractEvent implements TargetPlayerEvent {
    private final Player source;
    private final Group group;
    private final Cause cause;

    public GroupEvent(Player source, Group group, Cause cause) {
        this.source = source;
        this.group = group;
        this.cause = cause;
    }

    public final Group getGroup() {
        return group;
    }

    @Override
    public final Player getTargetEntity() {
        return source;
    }

    @Override
    public final Cause getCause() {
        return cause;
    }
}