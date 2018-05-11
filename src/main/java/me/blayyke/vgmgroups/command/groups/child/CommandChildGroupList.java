package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandChildGroupList extends ChildCommand {
    public CommandChildGroupList(@Nonnull VGMGroups plugin) {
        super(plugin, Lists.newArrayList("list"), Text.of("View a list of all the factions on the server"));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "list";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        List<Group> groups = GroupManager.getInstance().getGroups();
        PaginationList.Builder builder = PaginationList.builder();

        List<Text> contents = new ArrayList<>();
        for (Group group : groups) {
            contents.add(Text.builder().onHover(TextActions.showText(CommandChildGroupInfo.getGroupInfo(group))).append(
                    Text.of(TextColors.GREEN, group.getName()),
                    Text.of(TextColors.GRAY, " " + group.getOnlineMembers().size() + "/" + group.getMemberUUIDs().size() + " online")).build());
        }

        builder.title(Text.of(TextColors.GREEN, "Groups")).padding(Text.of("-")).contents(contents).sendTo(src);
        return CommandResult.success();
    }
}