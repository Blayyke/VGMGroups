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

public class CommandChildGroupLeader extends Command {
    public CommandChildGroupLeader(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("leader", "owner"), Text.of("Pass on ownership of your group."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "setleader";
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        final Player target = args.<Player>getOne("player")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        if (!group.getRank(player.getUniqueId()).getRank().isOwner()) {
            Texts.OWNER_ONLY.send(player);
            return CommandResult.empty();
        }

        group.setOwner(target.getUniqueId());
        Texts.LEADER_SET.sendWithVars(player, target.getName());
        Texts.LEADER_RECEIVE.sendWithVars(target, player.getName());

        return CommandResult.success();
    }
}