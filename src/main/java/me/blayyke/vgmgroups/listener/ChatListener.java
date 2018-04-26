package me.blayyke.vgmgroups.listener;

import me.blayyke.vgmgroups.channel.MessageChannelPrefixer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.channel.MessageChannel;

public class ChatListener extends VGMGListener {
    private MessageChannelPrefixer prefixerChannel = new MessageChannelPrefixer();

    @Listener
    public void onChat(MessageChannelEvent.Chat event, @Root Player player) {
        MessageChannel originalChannel = event.getOriginalChannel();
        MessageChannel newChannel = MessageChannel.combined(originalChannel, prefixerChannel);
        event.setChannel(newChannel);
    }
}