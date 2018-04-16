package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.relationship.GroupRelationship;
import me.blayyke.vgmgroups.relationship.Relationship;
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
        Group group = optGroup.orElse(null);
        Optional<Group> optTargetGroup = GroupManager.getInstance().getGroup(targetGroupUUID);
        Group targetGroup = optTargetGroup.orElse(null);

        return new GroupRelationship(group,targetGroup, relationship);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupRelationship obj, ConfigurationNode value) throws ObjectMappingException {
    }
}