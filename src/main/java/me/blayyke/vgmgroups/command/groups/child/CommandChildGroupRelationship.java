package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.enums.Relationship;
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

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupRelationship extends Command {
    public CommandChildGroupRelationship(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("relationship"), Text.of("Change or view your relationship with another group."));
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("relationship")))});
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "relationship";
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

        String name = args.<String>getOne("name").orElseThrow(() -> new CommandException(Text.of("name argument not present!")));
        Optional<String> relationshipOpt = args.getOne("relationship");

        Group target;

        Optional<Group> groupOpt = GroupManager.getInstance().getGroupByName(name);
        Optional<Player> targetPlayerOpt = Sponge.getServer().getPlayer(name);
        if (groupOpt.isPresent()) {
            target = groupOpt.get();
        } else if (targetPlayerOpt.isPresent()) {
            //find the targets group
            Player targetPlayer = targetPlayerOpt.get();
            Optional<Group> targetGroupOpt = GroupManager.getInstance().getPlayerGroup(targetPlayer);
            if (!targetGroupOpt.isPresent()) {
                Texts.PLAYER_NO_GROUP.send(player);
                return CommandResult.empty();
            }
            target = targetGroupOpt.get();
        } else {
            Texts.INPUT_NOT_FOUND.send(player);
            return CommandResult.empty();
        }

        if (relationshipOpt.isPresent()) {
            //set new relationship
            Relationship relationship = Relationship.fromString(relationshipOpt.get());
            if (relationship == null) {
                StringBuilder relationshipStr = new StringBuilder();
                for (Relationship relationship1 : Relationship.values())
                    relationshipStr.append(relationship1.getFriendlyName()).append(", ");

                error(Text.of("Invalid relationship setting. Valid settings are: " + relationshipStr.substring(0, relationshipStr.length() - 2)));
            }
            group.setRelationshipWith(target, relationship);
            Texts.RELATIONSHIP_UPDATE.broadcastWithVars(group, target.getName(), relationship.getFriendlyName());
            Texts.RELATIONSHIP_UPDATE_RECEIVE.broadcastWithVars(target, group.getName(), relationship.getFriendlyName());
            return CommandResult.success();
        }

        if (target.equals(group)) {
            Texts.CANNOT_TARGET_SELF.send(player);
            return CommandResult.empty();
        }

        //view current one
        Optional<GroupRelationship> relationshipWith = group.getRelationshipWith(target);
        Relationship relationship;
        if (!relationshipWith.isPresent()) relationship = Relationship.NEUTRAL;
        else relationship = relationshipWith.get().getRelationship();

        Texts.RELATIONSHIP_VIEW.sendWithVars(player, target.getName(), relationship.getFriendlyName());
        return CommandResult.success();
    }
}