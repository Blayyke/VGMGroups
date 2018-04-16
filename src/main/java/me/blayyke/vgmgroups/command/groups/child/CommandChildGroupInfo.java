package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
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
import org.spongepowered.api.text.action.TextActions;

import java.util.Optional;

public class CommandChildGroupInfo extends Command {
    public CommandChildGroupInfo() {
        super(Text.of("View yours or another groups stats / info."), "info", Lists.newArrayList());
    }

    @Override
    protected CommandResult runCommand(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!groupOpt.isPresent()) error("You are not in a group!");
        Group group = groupOpt.get();

        Text.builder().onHover(TextActions.showText(Text.of("hello!")));
        src.sendMessage(Text.of("Hover to view information"));

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.optional(GenericArguments.string(Text.of("group")))};
    }
}