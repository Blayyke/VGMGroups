package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class RankSerializer extends XTypeSerializer<Rank> {
    @Override
    public Rank deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return Rank.fromString(value.getNode("Rank").getString());
    }

    @Override
    public void serialize(TypeToken<?> type, Rank obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Rank").setValue(obj.toString());
    }
}