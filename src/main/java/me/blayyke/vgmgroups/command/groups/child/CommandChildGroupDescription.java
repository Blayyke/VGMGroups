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

public class CommandChildGroupDescription extends Command {
    public CommandChildGroupDescription(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("description", "desc", "d"), Text.of("Set the description for your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "description";
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{
                GenericArguments.remainingJoinedStrings(Text.of("desc"))
        });
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!groupOpt.isPresent()) error(Text.of("You are not in a group!"));
        Group group = groupOpt.get();

        final String desc = args.<String>getOne("desc")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        group.setDescription(desc);

        player.sendMessage(Text.builder()
                .append(Text.of(TextColors.GREEN, "Your group's description has been set to:"))
                .append(Text.of(TextColors.GRAY, desc)).toText());

        return CommandResult.success();
    }
}