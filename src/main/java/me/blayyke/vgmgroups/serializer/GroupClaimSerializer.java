package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.GroupClaim;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class GroupClaimSerializer implements TypeSerializer<GroupClaim> {
    @Override
    public GroupClaim deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int x = value.getNode("x").getInt();
        int z = value.getNode("z").getInt();
        UUID worldUUID = value.getNode("World").getValue(TypeToken.of(UUID.class));
        Optional<World> world = Sponge.getServer().getWorld(worldUUID);

        return new GroupClaim(world.orElseThrow(() -> new RuntimeException("World " + worldUUID + " not found!")), x, z);
    }

    @Override
    public void serialize(TypeToken<?> type, GroupClaim location, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("x").setValue(location.getChunkX());
        value.getNode("z").setValue(location.getChunkZ());
        value.getNode("World").setValue(TypeToken.of(UUID.class), location.getWorld().getUniqueId());
    }
}