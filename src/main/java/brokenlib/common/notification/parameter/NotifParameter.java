package brokenlib.common.notification.parameter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;
import java.util.function.Function;

public abstract class NotifParameter<A, B extends NBTBase> {

    private final Function<B, A> deserializer;
    private final Function<A, B> serializer;
    private final String name;
    private Optional<A> value;

    public NotifParameter(String name, Function<A, B> serializer, Function<B, A> deserializer) {
        this.name = name;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.value = Optional.empty();
    }

    public void setValue(A value) {
        this.value = Optional.of(value);
    }

    public String getName() {
        return this.name;
    }

    public Optional<A> getValue() {
        return this.value;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        if (this.value.isPresent())
            nbt.setTag(name, serializer.apply(this.value.get()));
    }

    @SuppressWarnings("unchecked")
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(name)) {
            NBTBase base = nbt.getTag(name);
            try {
                this.value = Optional.of(deserializer.apply((B) base));
            } catch (ClassCastException e) {}
        }
    }

    /**
     * @return true if the parameter should be sent on the client
     */
    public abstract boolean shouldBeSent();

    /**
     * @return true if the parameter can be override by client's response
     */
    public abstract boolean canBeOverride();

}
