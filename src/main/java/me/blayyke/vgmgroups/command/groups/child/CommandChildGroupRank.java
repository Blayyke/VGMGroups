package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRank;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.enums.Rank;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupRank extends Command {
    public CommandChildGroupRank(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("rank"), Text.of("Change or view a group member's rank."));
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{GenericArguments.onlyOne(GenericArguments.player(Text.of("member"))), GenericArguments.optional(GenericArguments.string(Text.of("rank")))});
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "rank";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
            Texts.OFFICER_ONLY.send(player);
            return CommandResult.empty();
        }

        Player target = args.<Player>getOne("name").orElseThrow(() -> new CommandException(Text.of("target argument not present!")));
        if (player.getUniqueId().equals(target.getUniqueId())) {
            Texts.CANNOT_TARGET_SELF.send(player);
            return CommandResult.empty();
        }

        Optional<String> rankOpt = args.getOne("rank");
        if (rankOpt.isPresent()) {
            //set new rank

            //ensure we aren't trying to change the owners rank.
            if (target.getUniqueId().equals(group.getOwnerUUID())) {
                Texts.CANNOT_CHANGE_OWNER_RANK.send(player);
                return CommandResult.empty();
            }

            Rank rank = Rank.fromString(rankOpt.get());
            if (rank == null) {
                StringBuilder rankStr = new StringBuilder();
                for (Rank rank1 : Rank.values())
                    rankStr.append(rank1.getFriendlyName()).append(", ");

                error(Text.of("Invalid rank setting. Valid settings are: " + rankStr.substring(0, rankStr.length() - 2)));
            }

            // make sure this player isnt a normie
            if (!group.getRank(player.getUniqueId()).getRank().isOfficer()) {
                Texts.OFFICER_ONLY.send(player);
                return CommandResult.empty();
            }

            // everything's good to go. do the stuff
            group.setRank(target.getUniqueId(), rank);
            Texts.RANK_UPDATE_SEND.sendWithVars(player, target.getName(), rank.getFriendlyName());
            Texts.RANK_UPDATE_RECEIVE.sendWithVars(target, rank.getFriendlyName());
            return CommandResult.success();
        }

        //view current rank
        GroupRank targetRank = group.getRank(target.getUniqueId());

        Texts.RANK_VIEW.sendWithVars(player, target.getName(), targetRank.getRank().getFriendlyName());
        return CommandResult.success();
    }
}