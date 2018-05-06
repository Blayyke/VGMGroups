package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;

abstract class XTypeSerializer<T> implements TypeSerializer<T> {
    <K> TypeToken<List<K>> listTokenType() {
        return new TypeToken<List<K>>() {
        };
    }
}
