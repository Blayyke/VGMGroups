package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.GroupClaim;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.UUID;

public class GroupClaimSerializer extends XTypeSerializer<GroupClaim> {
    @Override
    public GroupClaim deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int x = value.getNode("x").getInt();
        int z = value.getNode("z").getInt();
        UUID worldUUID = value.getNode("World").getValue(TypeToken.of(UUID.class));

        return new GroupClaim(worldUUID, x, z);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupClaim location, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("x").setValue(location.getChunkX());
        value.getNode("z").setValue(location.getChunkZ());
        value.getNode("World").setValue(TypeToken.of(UUID.class), location.getWorldUUID());
    }
}