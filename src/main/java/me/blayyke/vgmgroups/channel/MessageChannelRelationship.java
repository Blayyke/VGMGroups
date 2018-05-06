package me.blayyke.vgmgroups.channel;

import me.blayyke.vgmgroups.Group;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class MessageChannelRelationship implements MutableMessageChannel {
    private final Group group;
    private Set<MessageReceiver> members;

    public MessageChannelRelationship(Group group) {
        this.group = group;
        this.members = Collections.newSetFromMap(new WeakHashMap<>());
        members.addAll(group.getOnlineMembers());
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public boolean addMember(MessageReceiver member) {
        return members.add(member);
    }

    @Override
    public boolean removeMember(MessageReceiver member) {
        return members.remove(member);
    }

    @Override
    public void clearMembers() {
        members.clear();
    }
}