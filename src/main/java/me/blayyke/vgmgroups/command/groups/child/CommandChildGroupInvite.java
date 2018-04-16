package me.blayyke.vgmgroups.command.groups.child;

import me.blayyke.vgmgroups.Group;
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

import java.util.Arrays;
import java.util.Optional;

public class CommandChildGroupInvite extends Command {
    public CommandChildGroupInvite() {
        super(Text.of("Invite people to your group."), "groups.invite", Arrays.asList("invite", "add"));
    }

    @Override
    public CommandResult runCommand(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!groupOpt.isPresent()) error("You are not in a group!");
        Group group = groupOpt.get();

        final Player target = args.<Player>getOne("player")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        target.sendMessage(Text.of("You have been invited to " + group.getName() + " by " + player.getName() + "."));
        group.addInvited(target);

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
        };
    }
}