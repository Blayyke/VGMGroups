package me.blayyke.vgmgroups.command.groups.child;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.ChildCommand;
import me.blayyke.vgmgroups.enums.Channel;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;

public class CommandChildGroupChat extends ChildCommand {
    public CommandChildGroupChat(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("chat"), Text.of("Change your group chat."));
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.string(Text.of("channel"))};
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "chat";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);

        final String channelStr = args.<String>getOne("channel")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "argument missing")));
        Channel channel = Channel.fromString(channelStr);

        group.setChatChannel(player, channel);
        Texts.CHANNEL_UPDATED.sendWithVars(player, channel.getFriendlyName());
        return CommandResult.success();
    }
}