package me.blayyke.vgmgroups.listener;

import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class MoveListener extends VGMGListener {
    @Listener
    public void onMove(MoveEntityEvent event, @Root Player player) {
        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!groupOpt.isPresent()) return;
        Group group = groupOpt.get();

        Location<World> oldLocation = event.getFromTransform().getLocation();
        Location<World> newLocation = event.getToTransform().getLocation();
        World world = oldLocation.getExtent();

        Optional<Chunk> oldChunk = world.getChunk(oldLocation.getChunkPosition());
        Optional<Chunk> newChunk = world.getChunk(newLocation.getChunkPosition());
        if (!oldChunk.isPresent()) return;
        if (!newChunk.isPresent()) return;

        boolean equals = newChunk.get().equals(oldChunk.get());
        if (equals) return;

        Group groupForOldChunk = GroupManager.getInstance().getGroupForChunk(world, newChunk.get().getPosition());
        Group groupForChunk = GroupManager.getInstance().getGroupForChunk(world, newChunk.get().getPosition());
        if (groupForOldChunk == null && groupForChunk == null) return;

        if (groupForOldChunk != null && groupForOldChunk.equals(groupForChunk)) return;
        if (groupForChunk != null && groupForChunk.equals(groupForOldChunk)) return;

        player.sendMessage(Text.of("You have entered a new chunk owned by " + (groupForChunk == null ? "Wilderness" : groupForChunk.getName()) + "!"));
    }
}