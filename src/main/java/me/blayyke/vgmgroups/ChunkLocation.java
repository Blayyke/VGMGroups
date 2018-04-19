package me.blayyke.vgmgroups;

import java.util.UUID;

public class ChunkLocation {
    private final int x;
    private final int z;
    private final UUID worldUUID;

    public ChunkLocation(int x, int z, UUID worldUUID) {
        this.x = x;
        this.z = z;
        this.worldUUID = worldUUID;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof ChunkLocation
                && ((ChunkLocation) that).getX() == this.x
                && ((ChunkLocation) that).getZ() == this.z
                && ((ChunkLocation) that).getWorldUUID().equals(this.worldUUID);
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
}