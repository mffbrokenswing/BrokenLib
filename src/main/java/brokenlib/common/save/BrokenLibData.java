package brokenlib.common.save;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BrokenLibDatas extends WorldSavedData {

    private HashMap<String, Supplier<NBTTagCompound>> suppliers;
    private HashMap<String, Consumer<NBTTagCompound>> consumers;
    private NBTTagCompound loadedNBT = null;

    public BrokenLibDatas(String name) {
        super(name);
        this.suppliers = new HashMap<>();
        this.consumers = new HashMap<>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.loadedNBT = nbt;
        consumers.forEach((name, consumer) -> {
            if(this.loadedNBT.hasKey(name, Constants.NBT.TAG_COMPOUND)) {
                consumer.accept(this.loadedNBT.getCompoundTag(name));
            }
        });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();
        suppliers.forEach((name, supplier) -> {
            nbt.setTag(name, supplier.get());
        });
        return nbt;
    }

    public void addSubData(String name, Supplier<NBTTagCompound> supplier, Consumer<NBTTagCompound> consumer) {
        this.suppliers.put(name, supplier);
        this.consumers.put(name, consumer);
        if(this.loadedNBT != null && this.loadedNBT.hasKey(name, Constants.NBT.TAG_COMPOUND)) {
            consumer.accept(this.loadedNBT.getCompoundTag(name));
        }
    }

}
