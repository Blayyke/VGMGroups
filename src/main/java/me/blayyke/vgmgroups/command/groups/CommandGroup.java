package me.blayyke.vgmgroups.command.groups;

import com.google.common.collect.Sets;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.command.ParentCommand;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupCreate;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupInfo;
import me.blayyke.vgmgroups.command.groups.child.CommandChildGroupInvite;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;

public class CommandGroup extends ParentCommand {
    public CommandGroup() {
        super(Text.of("Main command"), "base", Arrays.asList("group", "groups"));
    }

    @Nonnull
    @Override
    protected Set<Command> registerChildren() {
        return Sets.newHashSet(
                new CommandChildGroupCreate(),
                new CommandChildGroupInvite(),
                new CommandChildGroupInfo()
        );
    }
}