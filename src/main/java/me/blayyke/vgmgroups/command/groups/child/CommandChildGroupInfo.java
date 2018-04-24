package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CommandChildGroupInfo extends Command {
    public CommandChildGroupInfo(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("info"), Text.of("View yours or another groups stats / info."));
    }

    @Override
    public Optional<CommandElement[]> getArguments() {
        return Optional.of(new CommandElement[]{GenericArguments.optional(GenericArguments.string(Text.of("name")))});
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
                sendGroupInfo(player, groupOpt.get());
                return CommandResult.success();
            }

            Optional<Player> targetOpt = Sponge.getServer().getPlayer(nameOpt.get());
            if (targetOpt.isPresent()) {
                Player target = targetOpt.get();
                Optional<Group> targetGroupOpt = GroupManager.getInstance().getGroupByUUID(target.getUniqueId());
                if (targetGroupOpt.isPresent()) {
                    Group targetGroup = targetGroupOpt.get();
                    sendGroupInfo(player, targetGroup);
                    return CommandResult.success();
                } else {
                    player.sendMessage(Text.of("That player is not in a group!"));
                    return CommandResult.empty();
                }
            }

            player.sendMessage(Text.of("That group does not exist or that player is not in a group!"));
            return CommandResult.empty();
        }

        Optional<Group> groupOpt = GroupManager.getInstance().getPlayerGroup(player);
        Group group = groupOpt.orElseThrow(() -> new CommandException(Text.of("You are not in a group!")));
        sendGroupInfo(player, group);
        return CommandResult.success();
    }

    private void sendGroupInfo(Player player, Group group) {
        Text newline = Text.of("\n");

        StringBuilder memberStrBuilder = new StringBuilder();
        for (Player member : group.getMembers()) {
            memberStrBuilder.append(group.getRank(member.getUniqueId()).getRank().getChatPrefix());
            memberStrBuilder.append(member.getName()).append(", ");
        }

        String memberStr = memberStrBuilder.toString();
        memberStr = memberStr.substring(0, memberStr.length() - 2);

        Text text = Text.builder()
                .append(Text.of("Group info for group " + group.getName()))
                .append(newline)

                .append(Text.of(TextColors.GREEN, TextStyles.BOLD, "Description: "))
                .append(Text.of(TextColors.GRAY, TextStyles.NONE, group.getDescription() == null ? "None." : group.getDescription()))
                .append(newline)

                .append(Text.of(TextColors.GREEN, TextStyles.BOLD, "Owner: "))
                .append(Text.of(TextColors.GRAY, TextStyles.NONE, group.getOwner().getName()))
                .append(newline)

                .append(Text.of(TextColors.GREEN, TextStyles.BOLD, "Members: "))
                .append(Text.of(TextColors.GRAY, TextStyles.NONE, memberStr))
                .build();

        player.sendMessage(text);
    }
}