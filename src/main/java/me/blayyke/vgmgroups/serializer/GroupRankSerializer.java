package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRank;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.manager.GroupManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.Optional;
import java.util.UUID;

public class GroupRankSerializer implements TypeSerializer<GroupRank> {
    @Override
    public GroupRank deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID groupUUID = value.getNode("Group").getValue(TypeToken.of(UUID.class));
        UUID memberUUID = value.getNode("Member").getValue(TypeToken.of(UUID.class));
        Rank rank = value.getNode("Rank").getValue(TypeToken.of(Rank.class));

        Optional<Group> optGroup = GroupManager.getInstance().getGroup(groupUUID);
        Group group = optGroup.orElseThrow(() -> new IllegalStateException("group not found!"));

        return new GroupRank(group, memberUUID, rank);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupRank groupRank, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Group").setValue(TypeToken.of(UUID.class), groupRank.getGroup().getUUID());
        value.getNode("Member").setValue(TypeToken.of(UUID.class), groupRank.getMemberUUID());
        value.getNode("Rank").setValue(TypeToken.of(Rank.class), groupRank.getRank());
    }
}