package brokenlib.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketUtils {

    public static void writeString(ByteBuf to, String text) {
        ByteBufUtils.writeUTF8String(to, text);
    }

     public static String readString(ByteBuf from) {
        return ByteBufUtils.readUTF8String(from);
    }

     public static void writeTag(ByteBuf to, NBTTagCompound nbt) {
        ByteBufUtils.writeTag(to, nbt);
    }

     public static NBTTagCompound readTag(ByteBuf from) {
        return ByteBufUtils.readTag(from);
    }

     public static void writeItemStack(ByteBuf to, ItemStack stack) {
        ByteBufUtils.writeItemStack(to, stack);
    }

     public static ItemStack readItemStack(ByteBuf from) {
        return ByteBufUtils.readItemStack(from);
    }

     public static void writeUUID(ByteBuf to, UUID uuid) {
        PacketBuffer packet = new PacketBuffer(to);
        packet.writeUniqueId(uuid);
    }

     public static UUID readUUID(ByteBuf from) {
        PacketBuffer packet = new PacketBuffer(from);
        return packet.readUniqueId();
    }

     public static <E extends Enum<E>> void writeEnum(ByteBuf to, E constant) {
        to.writeInt(constant.ordinal());
    }

     public static <E extends Enum<E>> E readEnum(ByteBuf from, Class<E> clazz) {
        return clazz.getEnumConstants()[from.readInt()];
    }

     public static <TYPE> void writeCollection(ByteBuf to, Collection<TYPE> collection, BiConsumer<ByteBuf, TYPE> processor) {
        to.writeInt(collection.size());
        collection.forEach(e -> processor.accept(to, e));
    }

     public static <TYPE> Collection<TYPE> readCollection(ByteBuf from, Function<ByteBuf, TYPE> processor) {
        LinkedList<TYPE> list = new LinkedList<>();
        int count = from.readInt();
        for (int i = 0; i < count; i++) {
            list.add(processor.apply(from));
        }
        return list;
    }

     public static <TYPE> void writeArray(ByteBuf to, TYPE[] elements, BiConsumer<ByteBuf, TYPE> processor) {
        LinkedList<TYPE> list = new LinkedList<>();
        Collections.addAll(list, elements);
        writeCollection(to, list, processor);
    }

    @SuppressWarnings("unchecked")
     public static <TYPE> TYPE[] readArray(ByteBuf from, Function<ByteBuf, TYPE> processor) {
        Collection<TYPE> collection = readCollection(from, processor);
        return (TYPE[]) collection.toArray();
    }

     public static <KEY, VALUE> void writeMap(ByteBuf to, Map<KEY, VALUE> map, BiConsumer<KEY, ByteBuf> keyProcessor, BiConsumer<VALUE, ByteBuf> valueProcessor) {
        to.writeInt(map.size());
        map.forEach((key, value) -> {
            keyProcessor.accept(key, to);
            valueProcessor.accept(value, to);
        });
    }

     public static <KEY, VALUE> Map<KEY, VALUE> readMap(ByteBuf from, Function<ByteBuf, KEY> keyProcessor, Function<ByteBuf, VALUE> valueProcessor) {
        int count = from.readInt();
        Map<KEY, VALUE> map = new HashMap<>();
        for (int i = 0; i < count; i++) {
            KEY key = keyProcessor.apply(from);
            VALUE value = valueProcessor.apply(from);
            map.put(key, value);
        }
        return map;
    }

}
