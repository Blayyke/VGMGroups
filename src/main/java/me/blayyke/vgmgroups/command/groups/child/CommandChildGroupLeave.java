package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

public class CommandChildGroupLeave extends ChildCommand {
    public CommandChildGroupLeave(@Nonnull VGMGroups plugin) {
        super(plugin, Lists.newArrayList("leave"), Text.of("Leave your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "leave";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        if (group.getRank(player.getUniqueId()).getRank().isOwner()) {
            Texts.OWNER_CANNOT_RUN.send(player);
            return CommandResult.empty();
        }

        Texts.GROUP_LEFT.send(player);
        group.removeMember(player);
        Texts.BROADCAST_GROUP_LEFT.broadcastWithVars(group, player.getName());
        return null;
    }
}