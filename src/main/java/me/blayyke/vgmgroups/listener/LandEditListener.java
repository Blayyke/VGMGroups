package me.blayyke.vgmgroups.listener;

import com.flowpowered.math.vector.Vector3i;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class LandEditListener extends VGMGListener {
    @Listener
    public void change(ChangeBlockEvent event, @Root Player player) {
        Location<World> location = player.getLocation();
        Vector3i chunkPosition = location.getChunkPosition();
        Group groupForChunk = GroupManager.getInstance().getGroupForChunk(location.getExtent(), chunkPosition);
        if (groupForChunk == null) return;
        if (groupForChunk.isInGroup(player)) return;

        // deny edit
        event.setCancelled(true);
        player.sendMessage(Text.of("You cannot damage another players owned land!"));
    }
}