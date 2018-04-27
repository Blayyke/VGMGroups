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
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.remainingJoinedStrings(Text.of("desc"))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
            Texts.OFFICER_ONLY.send(player);
            return CommandResult.empty();
        }

        final String desc = args.<String>getOne("desc")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        group.setDescription(desc);

        Texts.DESCRIPTION_UPDATE.sendWithVars(player, desc);
        return CommandResult.success();
    }
}