package me.blayyke.vgmgroups.serializer;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;

abstract class XTypeSerializer<K> implements TypeSerializer<K> {
    <T> TypeToken<List<T>> listTypeToken(Class<T> cl) {
        return new TypeToken<List<T>>() {
        }.where(new TypeParameter<T>() {
        }, cl);
    }
}
