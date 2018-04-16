package me.blayyke.vgmgroups.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.ChunkLocation;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.relationship.GroupRelationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;
import java.util.UUID;

public class GroupSerializer implements TypeSerializer<Group> {
    @Override
    public Group deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID player = value.getNode("UUID").getValue(TypeToken.of(UUID.class));
        UUID ownerUUID = value.getNode("Owner UUID").getValue(TypeToken.of(UUID.class));
        List<UUID> memberUUIDs = value.getNode("Members").getList(TypeToken.of(UUID.class));
        List<UUID> invitedUUIDs = value.getNode("Invited").getList(TypeToken.of(UUID.class));

        String name = value.getNode("Name").getString();
        String description = value.getNode("Description").getString();

        List<GroupRelationship> relationships = value.getNode("Relationships").getList(TypeToken.of(GroupRelationship.class));

        List<ChunkLocation> land = value.getNode("Land").getList(TypeToken.of(ChunkLocation.class));
        Vector3d homeLocation = value.getNode("Home").getValue(TypeToken.of(Vector3d.class));
        UUID homeWorld = value.getNode("Home World").getValue(TypeToken.of(UUID.class));

        return new Group(player, ownerUUID, memberUUIDs, name, description, relationships, land, homeLocation, homeWorld, invitedUUIDs);
    }

    @Override
    public void serialize(TypeToken<?> type, Group group, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("UUID").setValue(group.getUUID());
        value.getNode("Owner UUID").setValue(group.getOwnerUUID());
        value.getNode("Members").setValue(group.getMemberUUIDs());
        value.getNode("Invited").setValue(group.getInvitedUUIDs());

        value.getNode("Name").setValue(group.getName());
        value.getNode("Description").setValue(group.getDescription());

        value.getNode("Relationships").setValue(group.getRelationships());

        value.getNode("Land").setValue(group.getLand());
        value.getNode("Home").setValue(group.getHome());
        value.getNode("Home World").setValue(group.getHomeWorldUUID());
    }
}