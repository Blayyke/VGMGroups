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
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupCreate extends Command {
    public CommandChildGroupCreate(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("create"), Text.of("Create a group."));
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{GenericArguments.string(Text.of("name"))});
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "create";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);

        GroupManager manager = GroupManager.getInstance();
        Optional<Group> playerGroup = manager.getPlayerGroup(player);
        if (playerGroup.isPresent())
            throw new CommandException(Text.of(TextColors.RED, "You must leave your current group before you can create one!"));

        final String name = args.<String>getOne("name")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "argument missing")));
        manager.createNewGroup(player, name);

        player.sendMessage(Text.of(TextColors.GREEN, "Successfully created group " + name + "! Set your description with /group desc <desc>"));
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GRAY, player.getName() + " created the group " + name + "."));
        return CommandResult.success();
    }
}