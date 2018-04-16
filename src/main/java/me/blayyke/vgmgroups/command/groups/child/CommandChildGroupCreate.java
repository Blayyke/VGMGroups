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

import java.util.Collections;
import java.util.Optional;

public class CommandChildGroupCreate extends Command {
    public CommandChildGroupCreate() {
        super(Text.of("Create a group."), "groups.create", Collections.singletonList("create"));
    }

    @Override
    public CommandResult runCommand(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);

        GroupManager manager = GroupManager.getInstance();
        Optional<Group> playerGroup = manager.getPlayerGroup(player);
        if (playerGroup.isPresent())
            throw new CommandException(Text.of(TextColors.RED, "You must leave your current group before you can create one!"));

        final String name = args.<String>getOne("test")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "argument missing")));
        manager.createNewGroup(player, name);
        player.sendMessage(Text.of(TextColors.GREEN, "Successfully created group " + name + "!"));

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))
        };
    }
}