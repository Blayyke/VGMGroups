package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
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

public class CommandChildGroupClaim extends Command {
    public CommandChildGroupClaim(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("claim"), Text.of("Claim a chunk."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "claim";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Location<World> location = player.getLocation();
        Group group = requireGroup(player);
        Group groupForChunk = GroupManager.getInstance().getGroupForChunk(location.getExtent(), location.getChunkPosition());

        if (groupForChunk == null) {
            group.claimChunk(location);
            return CommandResult.success();
        }
        if (group.equals(groupForChunk)) {
            player.sendMessage(Text.of("Your group already owns this chunk."));
            return CommandResult.success();
        }

        error(Text.of("This chunk is claimed by " + groupForChunk.getName() + "!"));
        return CommandResult.success();
    }
}