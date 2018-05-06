package me.blayyke.vgmgroups.event;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

public abstract class GropEvent extends AbstractEvent implements TargetPlayerEvent {
    private final Player source;
    private final Group group;

    public GropEvent(Player source, Group group) {
        this.source = source;
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public Player getTargetEntity() {
        return source;
    }
}