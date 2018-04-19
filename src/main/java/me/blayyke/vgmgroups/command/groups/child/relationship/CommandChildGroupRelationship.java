package me.blayyke.vgmgroups.command.groups.child.relationship;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.enums.Relationship;
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
        Optional<Group> playerGroupOpt = GroupManager.getInstance().getPlayerGroup(player);
        if (!playerGroupOpt.isPresent()) error(Text.of("You are not in a group!"));
        Group playerGroup = playerGroupOpt.get();

        String name = args.<String>getOne("name").orElseThrow(() -> new CommandException(Text.of("name argument not present!")));
        Optional<String> relationshipOpt = args.getOne("relationship");

        Group target;

        Optional<Group> groupOpt = GroupManager.getInstance().getGroup(name);
        Optional<Player> targetPlayerOpt = Sponge.getServer().getPlayer(name);
        if (groupOpt.isPresent()) {
            target = groupOpt.get();
        } else if (targetPlayerOpt.isPresent()) {
            //find the targets group
            Player targetPlayer = targetPlayerOpt.get();
            Optional<Group> targetGroupOpt = GroupManager.getInstance().getPlayerGroup(targetPlayer);
            if (!targetGroupOpt.isPresent()) error(Text.of("That player is not in a group."));
            target = targetGroupOpt.get();
        } else
            throw new CommandException(Text.of("No player or group found with your input."));

        if (relationshipOpt.isPresent()) {
            //set new relationship
            Relationship relationship = Relationship.fromString(relationshipOpt.get());
            if (relationship == null) {
                StringBuilder relationshipStr = new StringBuilder();
                for (Relationship relationship1 : Relationship.values())
                    relationshipStr.append(relationship1.getFriendlyName()).append(", ");

                error(Text.of("Invalid relationship setting. Valid settings are: " + relationshipStr.substring(0, relationshipStr.length() - 2)));
            }
            playerGroup.setRelationshipWith(target, relationship);
            player.sendMessage(Text.of("Your relationship with " + target.getName() + " is now " + relationship.getFriendlyName() + "."));
            return CommandResult.success();
        }

        //view current one
        Optional<GroupRelationship> relationshipWith = playerGroup.getRelationshipWith(target);
        Relationship relationship;
        if (!relationshipWith.isPresent()) relationship = Relationship.NEUTRAL;
        else relationship = relationshipWith.get().getRelationship();

        player.sendMessage(Text.of("Your relationship with " + target.getName() + " is " + relationship.getFriendlyName() + "."));
        return CommandResult.success();
    }
}