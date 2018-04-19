package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.enums.Relationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.Optional;
import java.util.UUID;

public class GroupRelationshipSerializer implements TypeSerializer<GroupRelationship> {
    @Override
    public GroupRelationship deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID groupUUID = value.getNode("Group").getValue(TypeToken.of(UUID.class));
        UUID targetGroupUUID = value.getNode("Target Group").getValue(TypeToken.of(UUID.class));
        Relationship relationship = value.getNode("Relationship").getValue(TypeToken.of(Relationship.class));

        Optional<Group> optGroup = GroupManager.getInstance().getGroup(groupUUID);
        Group group = optGroup.orElseThrow(() -> new IllegalStateException("group not found!"));
        Optional<Group> optTargetGroup = GroupManager.getInstance().getGroup(targetGroupUUID);
        Group targetGroup = optTargetGroup.orElseThrow(() -> new IllegalStateException("target group not found!"));

        return new GroupRelationship(group, targetGroup, relationship);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupRelationship obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Group").setValue(TypeToken.of(UUID.class), obj.getGroup().getUUID());
        value.getNode("Target Group").setValue(TypeToken.of(UUID.class), obj.getTargetGroup().getUUID());
        value.getNode("Relationship").setValue(TypeToken.of(Relationship.class), obj.getRelationship());
    }
}