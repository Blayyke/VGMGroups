package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandChildGroupList extends Command {
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
        contents.add(Text.of("Item 1"));
        contents.add(Text.of("Item 2"));
        contents.add(Text.of("Item 3"));

        int pageNum = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (i % 10 == 0) pageNum++;

            contents.add(pageNum, Text.of(""));
        }

        builder.header(Text.of("Groups")).contents(contents).sendTo(src);
        return null;
    }
}