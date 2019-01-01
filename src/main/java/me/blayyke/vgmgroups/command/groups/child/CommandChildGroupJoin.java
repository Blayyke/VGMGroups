package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
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

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupJoin extends ChildCommand {
    public CommandChildGroupJoin(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("join"), Text.of("Join a group you have been invited to."));
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.optional(GenericArguments.string(Text.of("name")))};
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
        String name = nameOpt.orElseThrow(() -> new CommandException(Text.of("Missing argument")));
        Optional<Group> groupOpt = GroupManager.getInstance().getGroupByName(name);
        if (groupOpt.isPresent()) {
            attemptJoinGroup(player, groupOpt.get());
            return CommandResult.success();
        }

        Optional<Player> targetOpt = Sponge.getServer().getPlayer(name);
        if (targetOpt.isPresent()) {
            Player target = targetOpt.get();
            Optional<Group> targetGroupOpt = GroupManager.getInstance().getPlayerGroup(target);
            if (targetGroupOpt.isPresent()) {
                Group targetGroup = targetGroupOpt.get();
                attemptJoinGroup(player, targetGroup);
                return CommandResult.success();
            } else {
                Texts.PLAYER_NO_GROUP.send(player);
                return CommandResult.empty();
            }
        }

        Texts.INPUT_NOT_FOUND.send(player);
        return CommandResult.empty();
    }

    private void attemptJoinGroup(Player player, Group group) {
        if (!group.isInvited(player.getUniqueId())) {
            Texts.GROUP_NOT_INVITED.sendWithVars(player);
            return;
        }
        Texts.GROUP_OTHER_JOINED.broadcast(group);
        Texts.GROUP_JOINED.sendWithVars(player, group.getName());
        group.playerJoin(player);
    }
}