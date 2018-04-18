package brokenlib.common.utils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class NBTUtils {

    public static <KEY, VALUE, KEY_TYPE extends NBTBase, VALUE_TYPE extends NBTBase> NBTTagList writeMap(Map<KEY, VALUE> map, Function<KEY, KEY_TYPE> keyWriter, Function<VALUE, VALUE_TYPE> valueWriter) {
        NBTTagList serializedMap = new NBTTagList();
        map.forEach((key, value) -> {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag("key", keyWriter.apply(key));
            entry.setTag("value", valueWriter.apply(value));
            serializedMap.appendTag(entry);
        });
        return serializedMap;
    }

    @SuppressWarnings("unchecked")
    public static <KEY, VALUE, KEY_TYPE extends NBTBase, VALUE_TYPE extends NBTBase> void readMap(Map<KEY, VALUE> toFill, NBTTagList serializedMap, Function<KEY_TYPE, KEY> keyReader, Function<VALUE_TYPE, VALUE> valueReader) {
        for(int i = 0; i < serializedMap.tagCount(); i++) {
            NBTTagCompound entry = serializedMap.getCompoundTagAt(i);
            KEY key = keyReader.apply((KEY_TYPE) entry.getTag("key"));
            VALUE value = valueReader.apply((VALUE_TYPE) entry.getTag("value"));
            toFill.put(key, value);
        }
    }

    public static <T, ST extends NBTBase> NBTTagList writeIterable(Iterable<T> iterable, Function<T, ST> entryWriter) {
        NBTTagList list = new NBTTagList();
        iterable.forEach(entry -> {
            list.appendTag(entryWriter.apply(entry));
        });
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T, ST extends NBTBase> void readCollection(Collection<T> collection, NBTTagList serializedIterable, Function<ST, T> entryReader) {
        serializedIterable.forEach(entry -> {
            collection.add(entryReader.apply((ST)entry));
        });
    }
}
