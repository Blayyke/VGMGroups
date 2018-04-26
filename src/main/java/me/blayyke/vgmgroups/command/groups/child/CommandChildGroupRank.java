package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRank;
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

        Player target = args.<Player>getOne("name").orElseThrow(() -> new CommandException(Text.of("target argument not present!")));

        Optional<String> rankOpt = args.getOne("rank");
        if (rankOpt.isPresent()) {
            //set new rank

            //ensure we aren't trying to change the owners rank.
            if (target.getUniqueId().equals(group.getOwnerUUID()))
                error(Text.of("Cannot change rank for owner."));

            Rank rank = Rank.fromString(rankOpt.get());
            if (rank == null) {
                StringBuilder rankStr = new StringBuilder();
                for (Rank rank1 : Rank.values())
                    rankStr.append(rank1.getFriendlyName()).append(", ");

                error(Text.of("Invalid rank setting. Valid settings are: " + rankStr.substring(0, rankStr.length() - 2)));
            }

            // make sure this player isnt a normie
            if (!group.getRank(player.getUniqueId()).getRank().canManageRanks(rank))
                error(Text.of("You cannot promote / demote players!"));

            // everything's good to go. do the stuff
            group.setRank(target.getUniqueId(), rank);
            player.sendMessage(Text.of(target.getName() + "'s rank is now " + rank.getFriendlyName() + "."));
            return CommandResult.success();
        }

        //view current rank
        GroupRank targetRank = group.getRank(target.getUniqueId());

        player.sendMessage(Text.of(target.getName() + "'s rank is " + targetRank.getRank().getFriendlyName() + "."));
        return CommandResult.success();
    }
}