package me.blayyke.vgmgroups;

import me.blayyke.vgmgroups.enums.Rank;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class GroupRank {
    private UUID playerUUID;
    private Rank rank;

    public GroupRank(UUID playerUUID, Rank rank) {
        this.playerUUID = playerUUID;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public UUID getMemberUUID() {
        return playerUUID;
    }
}