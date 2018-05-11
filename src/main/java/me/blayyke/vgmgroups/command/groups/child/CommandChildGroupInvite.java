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
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        final Player target = args.<Player>getOne("player")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing argument")));

        if (player.getUniqueId().equals(target.getUniqueId())) {
            Texts.CANNOT_TARGET_SELF.send(player);
            return CommandResult.empty();
        }

        if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
            Texts.OFFICER_ONLY.send(player);
            return CommandResult.empty();
        }

        if (group.isInvited(target.getUniqueId())) {
            Texts.INVITATION_REVOKED.sendWithVars(player, target.getName());
            group.revokeInvite(target.getUniqueId());
            return CommandResult.success();
        }
        Texts.INVITATION_SENT.broadcastWithVars(group, target.getName());
        group.invitePlayer(player, target);
        Texts.INVITATION_RECEIVED.sendWithVars(target, group.getName(), player.getName());

        return CommandResult.success();
    }
}