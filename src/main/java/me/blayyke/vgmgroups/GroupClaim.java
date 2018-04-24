package me.blayyke.vgmgroups;

import org.spongepowered.api.world.World;

public class GroupClaim {
    private World world;

    private int chunkX;
    private int chunkY;

    public GroupClaim(World world, int chunkX, int chunkY) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkY;
    }

    public World getWorld() {
        return world;
    }
}