package me.blayyke.vgmgroups.command.groups;

import com.google.common.collect.Sets;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.command.CommandContainer;
import me.blayyke.vgmgroups.command.groups.child.*;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupRank;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupRelationship;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;

public class CommandGroup extends CommandContainer {
    public CommandGroup(VGMGroups plugin) {
        super(plugin, Arrays.asList("group", "groups"), Text.of("Base command"));
    }

    @Nonnull
    @Override
    protected Set<Command> registerChildren() {
        return Sets.newHashSet(
                new CommandChildGroupChat(getPlugin()),
                new CommandChildGroupClaim(getPlugin()),
                new CommandChildGroupCreate(getPlugin()),
                new CommandChildGroupDescription(getPlugin()),
                new CommandChildGroupDisband(getPlugin()),
                new CommandChildGroupInfo(getPlugin()),
                new CommandChildGroupInvite(getPlugin()),
                new CommandChildGroupJoin(getPlugin()),
                new CommandChildGroupLeader(getPlugin()),
                new CommandChildGroupList(getPlugin()),
                new CommandChildGroupName(getPlugin()),
                new CommandChildGroupRank(getPlugin()),
                new CommandChildGroupRelationship(getPlugin())
        );
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "base";
    }
}