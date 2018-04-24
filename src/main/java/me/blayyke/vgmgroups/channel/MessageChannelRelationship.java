package me.blayyke.vgmgroups.channel;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.ArrayList;
import java.util.Collection;

public class MessageChannelRelationship implements MessageChannel {
    private final Group group;

    public MessageChannelRelationship(Group group) {
        this.group = group;
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return new ArrayList<>(group.getOnlineMembers());
    }
}