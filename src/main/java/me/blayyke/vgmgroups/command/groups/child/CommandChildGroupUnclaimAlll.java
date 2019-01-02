package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;

public class CommandChildGroupUnclaimAlll extends ChildCommand {
    public CommandChildGroupUnclaimAlll(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("unclaimall"), Text.of("Unclaim all of your groups claimed chunks."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "unclaimall";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Location<World> location = player.getLocation();
        Group group = requireGroup(player);

        if (!group.getRank(player.getUniqueId()).getRank().isOwner()) {
            Texts.OWNER_ONLY.send(player);
            return CommandResult.empty();
        }

        group.unclaimAllChunks();
        Texts.UNCLAIM_ALL_SUCCESS.sendWithVars(player, location.getChunkPosition().getX(), location.getChunkPosition().getZ());
        return CommandResult.success();
    }
}