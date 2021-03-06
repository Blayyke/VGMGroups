package me.blayyke.vgmgroups.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupClaim;
import me.blayyke.vgmgroups.GroupRank;
import me.blayyke.vgmgroups.GroupRelationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.UUID;

public class GroupSerializer extends XTypeSerializer<Group> {
    @Override
    public Group deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        long creationTime = value.getNode("Created At").getLong();

        UUID groupUUID = value.getNode("UUID").getValue(TypeToken.of(UUID.class));
        UUID ownerUUID = value.getNode("Owner UUID").getValue(TypeToken.of(UUID.class));
        UUID homeWorld = value.getNode("Home World").getValue(TypeToken.of(UUID.class));
        List<UUID> memberUUIDs = value.getNode("Members").getList(TypeToken.of(UUID.class));
        List<UUID> invitedUUIDs = value.getNode("Invited").getList(TypeToken.of(UUID.class));

        String name = value.getNode("Name").getString();
        String description = value.getNode("Description").getString();

        List<GroupRelationship> relationships = value.getNode("Relationships").getList(TypeToken.of(GroupRelationship.class));
        List<GroupRank> ranks = value.getNode("Ranks").getList(TypeToken.of(GroupRank.class));

        List<GroupClaim> land = value.getNode("Land").getList(TypeToken.of(GroupClaim.class));
        Vector3d homeLocation = value.getNode("Home").getValue(TypeToken.of(Vector3d.class));

        return new Group(ownerUUID, creationTime, memberUUIDs, name, description, relationships, ranks, land, homeLocation, homeWorld, invitedUUIDs, groupUUID);
    }

    @Override
    public void serialize(TypeToken<?> type, Group group, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Created At").setValue(group.getCreationTime());

        value.getNode("UUID").setValue(TypeToken.of(UUID.class), group.getUUID());
        value.getNode("Owner UUID").setValue(TypeToken.of(UUID.class), group.getOwnerUUID());
        value.getNode("Home World").setValue(TypeToken.of(UUID.class), group.getHomeWorldUUID());
        value.getNode("Members").setValue(listTypeToken(UUID.class), group.getMemberUUIDs());
        value.getNode("Invited").setValue(listTypeToken(UUID.class), group.getInvitedUUIDs());
        value.getNode("Name").setValue(TypeToken.of(String.class), group.getName());
        value.getNode("Description").setValue(TypeToken.of(String.class), group.getDescription());

        value.getNode("Relationships").setValue(listTypeToken(GroupRelationship.class), group.getRelationships());
        value.getNode("Ranks").setValue(listTypeToken(GroupRank.class), group.getRanks());

        value.getNode("Land").setValue(listTypeToken(GroupClaim.class), group.getClaims());
        value.getNode("Home").setValue(TypeToken.of(Vector3d.class), group.getHome());
    }
}