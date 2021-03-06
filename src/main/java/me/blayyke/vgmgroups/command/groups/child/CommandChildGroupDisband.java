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

import javax.annotation.Nonnull;

public class CommandChildGroupDisband extends ChildCommand {
    public CommandChildGroupDisband(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("disband"), Text.of("Disband your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "disband";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        if (!group.getRank(player.getUniqueId()).getRank().isOwner()) {
            Texts.OWNER_ONLY.send(player);
            return CommandResult.empty();
        }

        GroupManager.getInstance().deleteGroup(group);
        Texts.GROUP_DISBANDED.send(player);

        return CommandResult.success();
    }
}