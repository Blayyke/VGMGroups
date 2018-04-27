package me.blayyke.vgmgroups.command.groups.child.home;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;

public class CommandChildGroupSetHome extends Command {
    public CommandChildGroupSetHome(@Nonnull VGMGroups plugin) {
        super(plugin, Lists.newArrayList("sethome"), Text.of("Update your groups home."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "sethome";
    }

    @Nonnull
    @Override
    protected CommandElement[] getArguments() {
        return new CommandElement[]{};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);
        Location<World> location = player.getLocation();

        if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
            Texts.OFFICER_ONLY.send(player);
            return CommandResult.empty();
        }

        Group groupForChunk = GroupManager.getInstance().getGroupForChunk(location.getExtent(), location.getChunkPosition());
        if (groupForChunk == null) {
            Texts.CHUNK_NOT_OWNED.sendWithVars(player);
            return CommandResult.empty();
        }

        group.setHome(location);
        Texts.HOME_SET.sendWithVars(player, location.getX(), location.getY(), location.getZ(), location.getExtent().getName());
        return CommandResult.success();
    }
}