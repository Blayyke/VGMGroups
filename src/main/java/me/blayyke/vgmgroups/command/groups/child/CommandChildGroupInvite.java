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
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupInvite extends Command {
    public CommandChildGroupInvite(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("invite", "add"), Text.of("Invite people to your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "invite";
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{
                GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
        });
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!groupOpt.isPresent()) error(Text.of("You are not in a group!"));
        Group group = groupOpt.get();

        final Player target = args.<Player>getOne("player")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        group.broadcastMessage(Text.of(target.getName() + " has been invited to your group."));
        group.addInvited(target);
        target.sendMessage(Text.of("You have been invited to " + group.getName() + " by " + player.getName() + "."));

        return CommandResult.success();
    }
}