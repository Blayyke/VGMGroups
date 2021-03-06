package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
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
import java.util.regex.Pattern;

public class CommandChildGroupName extends ChildCommand {
    private int minNameLength = 2; //todo read from config
    private int maxNameLength = 16; //todo read from config
    private Pattern nameRegex = Pattern.compile("^[a-zA-Z0-9_-]*$"); //todo read from config

    public CommandChildGroupName(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("name", "n"), Text.of("Set the name for your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "rename";
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.remainingJoinedStrings(Text.of("desc"))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        final String name = args.<String>getOne("desc")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        if (name.length() > maxNameLength) {
            Texts.NAME_TOO_LONG.send(player);
            return CommandResult.empty();
        }

        if (name.length() < minNameLength) {
            Texts.NAME_TOO_SHORT.send(player);
            return CommandResult.empty();
        }

        if (!nameRegex.matcher(name).matches()) {
            Texts.INVALID_NAME.send(player);
            return CommandResult.empty();
        }

        group.setName(name);
        Texts.NAME_UPDATED.broadcastWithVars(group, name);
        return CommandResult.success();
    }
}