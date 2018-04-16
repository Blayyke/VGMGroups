package me.blayyke.vgmgroups.listener;

import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

public class DamageListener {
    @Listener
    public void handle(DamageEntityEvent event, @Root Player source) {
        if (!(event.getTargetEntity() instanceof Player)) return;
        Player target = (Player) event.getTargetEntity();

        Optional<Group> targetGroup = GroupManager.getInstance().getPlayerGroup(target);
        Optional<Group> sourceGroup = GroupManager.getInstance().getPlayerGroup(target);
        if (!targetGroup.isPresent()) return;
        if (!sourceGroup.isPresent()) return;

        if (targetGroup.get().equals(sourceGroup.get())) {
            //tried to attack fellow group member, cancel damage.
            source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&cYou cannot hit a group member!"));
            event.setCancelled(true);
        }
    }
}