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

public class CommandChildGroupClaim extends ChildCommand {
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

        if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
            Texts.OFFICER_ONLY.send(player);
            return CommandResult.empty();
        }

        if (groupForChunk == null) {
            if (group.claimChunk(location)) {
                Texts.CLAIM_SUCCESS.sendWithVars(player, location.getChunkPosition().getX(), location.getChunkPosition().getZ());
                return CommandResult.success();
            } else {
                Texts.CLAIM_FAILURE.send(player);
                return CommandResult.empty();
            }
        }
        if (group.equals(groupForChunk)) {
            Texts.CLAIMED_SELF.send(player);
            return CommandResult.empty();
        }

        Texts.CLAIMED_OTHER.sendWithVars(player, groupForChunk.getName());
        return CommandResult.empty();
    }
}