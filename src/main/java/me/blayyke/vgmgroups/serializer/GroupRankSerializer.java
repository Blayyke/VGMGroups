package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.GroupRank;
import me.blayyke.vgmgroups.enums.Rank;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.UUID;

public class GroupRankSerializer extends XTypeSerializer<GroupRank> {
    @Override
    public GroupRank deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID memberUUID = value.getNode("Member").getValue(TypeToken.of(UUID.class));
        Rank rank = value.getNode("Rank").getValue(TypeToken.of(Rank.class));

        return new GroupRank(memberUUID, rank);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupRank groupRank, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Member").setValue(TypeToken.of(UUID.class), groupRank.getMemberUUID());
        value.getNode("Rank").setValue(TypeToken.of(Rank.class), groupRank.getRank());
    }
}