package me.blayyke.vgmgroups.command.groups;

import com.google.common.collect.Sets;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
import me.blayyke.vgmgroups.command.CommandContainer;
import me.blayyke.vgmgroups.command.groups.child.*;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupRank;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupRelationship;
import me.blayyke.vgmgroups.command.groups.child.home.CommandChildGroupHome;
import me.blayyke.vgmgroups.command.groups.child.home.CommandChildGroupSetHome;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;

public class CommandGroup extends CommandContainer {
    public CommandGroup(VGMGroups plugin) {
        super(plugin, Arrays.asList("group", "groups", "g", "fac", "faction", "factions", "f"), Text.of("Base command"));
    }

    @Nonnull
    @Override
    protected Set<ChildCommand> registerChildren() {
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
                new CommandChildGroupLeave(getPlugin()),
                new CommandChildGroupList(getPlugin()),
                new CommandChildGroupName(getPlugin()),
                new CommandChildGroupMap(getPlugin()),
                new CommandChildGroupRank(getPlugin()),
                new CommandChildGroupRelationship(getPlugin()),
                new CommandChildGroupHome(getPlugin()),
                new CommandChildGroupSetHome(getPlugin()),
                new CommandChildGroupUnclaim(getPlugin()),
                new CommandChildGroupUnclaimAlll(getPlugin())
        );
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "base";
    }
}