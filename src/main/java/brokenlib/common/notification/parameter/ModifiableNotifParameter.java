package brokenlib.common.notification.parameter;

import net.minecraft.nbt.NBTBase;

import java.util.function.Function;

public class ModifiableNotifParameter<A, B extends NBTBase> extends NotifParameter<A, B> {

    public ModifiableNotifParameter(String name, Function<A, B> serializer, Function<B, A> deserializer) {
        super(name, serializer, deserializer);
    }

    @Override
    public boolean shouldBeSent() {
        return true;
    }

    @Override
    public boolean canBeOverride() {
        return true;
    }
}
