package me.blayyke.vgmgroups.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.ChunkLocation;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRank;
import me.blayyke.vgmgroups.GroupRelationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;
import java.util.UUID;

public class GroupSerializer implements TypeSerializer<Group> {
    @Override
    public Group deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID groupUUID = value.getNode("UUID").getValue(TypeToken.of(UUID.class));
        UUID ownerUUID = value.getNode("Owner UUID").getValue(TypeToken.of(UUID.class));
        UUID homeWorld = value.getNode("Home World").getValue(TypeToken.of(UUID.class));
        List<UUID> memberUUIDs = value.getNode("Members").getList(TypeToken.of(UUID.class));
        List<UUID> invitedUUIDs = value.getNode("Invited").getList(TypeToken.of(UUID.class));

        String name = value.getNode("Name").getString();
        String description = value.getNode("Description").getString();

        List<GroupRelationship> relationships = value.getNode("Relationships").getList(TypeToken.of(GroupRelationship.class));
        List<GroupRank> ranks = value.getNode("Ranks").getList(TypeToken.of(GroupRank.class));

        List<ChunkLocation> land = value.getNode("Land").getList(TypeToken.of(ChunkLocation.class));
        Vector3d homeLocation = value.getNode("Home").getValue(TypeToken.of(Vector3d.class));

        return new Group(groupUUID, ownerUUID, memberUUIDs, name, description, relationships, ranks, land, homeLocation, homeWorld, invitedUUIDs);
    }

    @Override
    public void serialize(TypeToken<?> type, Group group, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("UUID").setValue(TypeToken.of(UUID.class), group.getUUID());
        value.getNode("Owner UUID").setValue(TypeToken.of(UUID.class), group.getOwnerUUID());
        value.getNode("Home World").setValue(TypeToken.of(UUID.class), group.getHomeWorldUUID());
        value.getNode("Members").setValue(new TypeToken<List<UUID>>() {
        }, group.getMemberUUIDs());
        value.getNode("Invited").setValue(new TypeToken<List<UUID>>() {
        }, group.getInvitedUUIDs());
        value.getNode("Name").setValue(TypeToken.of(String.class), group.getName());
        value.getNode("Description").setValue(TypeToken.of(String.class), group.getDescription());

        value.getNode("Relationships").setValue(new TypeToken<List<GroupRelationship>>() {
        }, group.getRelationships());
        value.getNode("Ranks").setValue(new TypeToken<List<GroupRank>>() {
        }, group.getRanks());

        value.getNode("Land").setValue(new TypeToken<List<ChunkLocation>>() {
        }, group.getLand());
        value.getNode("Home").setValue(TypeToken.of(Vector3d.class), group.getHome());
    }
}