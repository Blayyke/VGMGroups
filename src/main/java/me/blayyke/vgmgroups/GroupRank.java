package me.blayyke.vgmgroups;

import me.blayyke.vgmgroups.enums.Rank;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class GroupRank {
    private final Group group;
    private UUID playerUUID;
    private Rank rank;

    public GroupRank(Group group, UUID playerUUID, Rank rank) {
        this.group = group;
        this.playerUUID = playerUUID;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public UUID getMemberUUID() {
        return playerUUID;
    }

    public Group getGroup() {
        return group;
    }
}