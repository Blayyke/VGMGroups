package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.enums.Relationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.UUID;

public class GroupRelationshipSerializer extends XTypeSerializer<GroupRelationship> {
    @Override
    public GroupRelationship deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID groupUUID = value.getNode("Group").getValue(TypeToken.of(UUID.class));
        UUID targetGroupUUID = value.getNode("Target Group").getValue(TypeToken.of(UUID.class));
        Relationship relationship = value.getNode("Relationship").getValue(TypeToken.of(Relationship.class));

        return new GroupRelationship(groupUUID, targetGroupUUID, relationship);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupRelationship obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Group").setValue(TypeToken.of(UUID.class), obj.getGroupUUID());
        value.getNode("Target Group").setValue(TypeToken.of(UUID.class), obj.getTargetGroupUUID());
        value.getNode("Relationship").setValue(TypeToken.of(Relationship.class), obj.getRelationship());
    }
}