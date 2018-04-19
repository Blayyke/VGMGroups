package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.enums.Relationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class RelationshipSerializer implements TypeSerializer<Relationship> {
    @Override
    public Relationship deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
            return Relationship.fromString(value.getNode("Relationship").getString());
    }

    @Override
    public void serialize(TypeToken<?> type, Relationship obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("Relationship").setValue(obj.toString());
    }
}