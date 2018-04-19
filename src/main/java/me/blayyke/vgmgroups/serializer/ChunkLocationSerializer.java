package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.ChunkLocation;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.UUID;

public class ChunkLocationSerializer implements TypeSerializer<ChunkLocation> {
    @Override
    public ChunkLocation deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int x = value.getNode("x").getInt();
        int z = value.getNode("z").getInt();
        UUID worldUUID = value.getNode("World").getValue(TypeToken.of(UUID.class));

        return new ChunkLocation(x, z, worldUUID);
    }

    @Override
    public void serialize(TypeToken<?> type, ChunkLocation location, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("x").setValue(location.getX());
        value.getNode("z").setValue(location.getZ());
        value.getNode("World").setValue(TypeToken.of(UUID.class), location.getWorldUUID());
    }
}