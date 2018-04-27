package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupInfo extends Command {
    public CommandChildGroupInfo(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("info"), Text.of("View yours or another groups stats / info."));
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.optional(GenericArguments.string(Text.of("name")))};
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "info";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Optional<String> nameOpt = args.getOne("name");
        if (nameOpt.isPresent()) {
            String name = nameOpt.get();
            Optional<Group> groupOpt = GroupManager.getInstance().getGroupByName(name);
            if (groupOpt.isPresent()) {
                player.sendMessage(getGroupInfo(groupOpt.get()));
                return CommandResult.success();
            }

            Optional<Player> targetOpt = Sponge.getServer().getPlayer(nameOpt.get());
            if (targetOpt.isPresent()) {
                Player target = targetOpt.get();
                Optional<Group> targetGroupOpt = GroupManager.getInstance().getGroupByUUID(target.getUniqueId());
                if (targetGroupOpt.isPresent()) {
                    Group targetGroup = targetGroupOpt.get();
                    player.sendMessage(getGroupInfo(targetGroup));
                    return CommandResult.success();
                } else {
                    Texts.PLAYER_NO_GROUP.send(player);
                    return CommandResult.empty();
                }
            }

            Texts.INPUT_NOT_FOUND.send(player);
            return CommandResult.empty();
        }

        Group group = requireGroup(player);
        player.sendMessage(getGroupInfo(group));
        return CommandResult.success();
    }

    public static Text getGroupInfo(Group group) {
        Text newline = Text.of("\n");

        StringBuilder memberStrBuilder = new StringBuilder();
        StringBuilder offlineMemberStrBuilder = new StringBuilder();
        int online = 0;
        int offline = 0;

        for (User member : group.getMembers()) {
            if (member.isOnline()) {
                memberStrBuilder.append(group.getRank(member.getUniqueId()).getRank().getChatPrefix());
                memberStrBuilder.append(member.getName()).append(", ");
                online++;
                continue;
            }
            offlineMemberStrBuilder.append(group.getRank(member.getUniqueId()).getRank().getChatPrefix());
            offlineMemberStrBuilder.append(member.getName()).append(", ");
            offline++;
        }

        String memberStr = memberStrBuilder.toString();
        memberStr = memberStr.endsWith(", ") ? memberStr.substring(0, memberStr.length() - 2) : memberStr;
        String offlineMemberStr = offlineMemberStrBuilder.toString();
        offlineMemberStr = offlineMemberStr.endsWith(", ") ? offlineMemberStr.substring(0, offlineMemberStr.length() - 2) : offlineMemberStr;

        if (memberStr.isEmpty()) memberStr = "None";
        if (offlineMemberStr.isEmpty()) offlineMemberStr = "None";

        return Text.builder()
                .append(Text.of("Group " + group.getName()))

                .append(newline)

                .append(Text.of(TextColors.GREEN, "Description: "))
                .append(Text.of(TextColors.GRAY, group.getDescription() == null ? "None." : group.getDescription()))

                .append(newline)

                .append(Text.of(TextColors.GREEN, "Members online (" + online + "): "))
                .append(newline)
                .append(Text.of(TextColors.GRAY, memberStr))

                .append(newline)

                .append(Text.of(TextColors.GREEN, "Members offline (" + offline + "): "))
                .append(newline)
                .append(Text.of(TextColors.GRAY, offlineMemberStr))
                .build();
    }
}