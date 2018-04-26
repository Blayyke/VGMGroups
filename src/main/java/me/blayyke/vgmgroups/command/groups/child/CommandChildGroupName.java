package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
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

public class CommandChildGroupName extends Command {
    public CommandChildGroupName(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("name", "n"), Text.of("Set the name for your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "rename";
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
        Group group = requireGroup(player);

        final String name = args.<String>getOne("desc")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        group.setName(name);
        Texts.NAME_UPDATED.broadcastWithVars(group, name);
        return CommandResult.success();
    }
}