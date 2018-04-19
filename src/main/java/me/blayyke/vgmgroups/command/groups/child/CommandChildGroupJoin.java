package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupJoin extends Command {
    public CommandChildGroupJoin(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("join"), Text.of("Join a group you have been invited to."));
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{GenericArguments.optional(GenericArguments.string(Text.of("name")))});
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "join";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<String> nameOpt = args.getOne("name");
        String name = nameOpt.orElseThrow(() -> new CommandException(Text.of("")));
        Optional<Group> groupOpt = GroupManager.getInstance().getGroup(name);
        if (groupOpt.isPresent()) {
            attemptJoinGroup(player, groupOpt.get());
            return CommandResult.success();
        }

        Optional<Player> targetOpt = Sponge.getServer().getPlayer(name);
        if (targetOpt.isPresent()) {
            Player target = targetOpt.get();
            Optional<Group> targetGroupOpt = GroupManager.getInstance().getGroup(target.getUniqueId());
            if (targetGroupOpt.isPresent()) {
                Group targetGroup = targetGroupOpt.get();
                attemptJoinGroup(player, targetGroup);
                return CommandResult.success();
            } else {
                player.sendMessage(Text.of("That player is not in a group!"));
                return CommandResult.empty();
            }
        }

        player.sendMessage(Text.of("That group does not exist or that player is not in a group!"));
        return CommandResult.empty();
    }

    private void attemptJoinGroup(Player player, Group group) throws CommandException {
        if (!group.isInvited(player.getUniqueId()))
            throw new CommandException(Text.of("You are not invited to this group."));
        group.playerJoin(player);
    }
}