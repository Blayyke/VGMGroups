package me.blayyke.vgmgroups;

import java.util.UUID;

public class GroupClaim {
    private UUID worldUUID;

    private int chunkX;
    private int chunkY;

    public GroupClaim(UUID worldUUID, int chunkX, int chunkY) {
        this.worldUUID = worldUUID;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkY;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
}