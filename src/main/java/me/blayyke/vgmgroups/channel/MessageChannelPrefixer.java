package me.blayyke.vgmgroups.channel;

import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class MessageChannelPrefixer implements MessageChannel {
    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        if (sender == null) return Optional.ofNullable(original);
        Player player = (Player) sender;
        Optional<Group> playerGroup = GroupManager.getInstance().getPlayerGroup(player);
        if (!playerGroup.isPresent()) return Optional.of(original);
        Group group = playerGroup.get();

        Text text = Text.builder()
                .append(Text.of(TextColors.GREEN, "[" + group.getRank(player.getUniqueId()).getRank().getChatPrefix()))
                .append(Text.of(TextColors.GREEN, group.getName() + "] "))
                .append(original)
                .toText();
        return Optional.of(text);
    }
}