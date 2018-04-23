package brokenlib.common.save;

import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.lang.reflect.InvocationTargetException;

public class WorldDataWrapper<T extends WorldSavedData> {

    private String identifier;
    private Class<T> clazz;

    private T data;

    public WorldDataWrapper(Class<T> clazz, String identifier) {
        this.clazz = clazz;
        this.data = null;
        this.identifier = identifier;
    }
    
    public T getData() {
        return data;
    }

    public void serverStarting() {
        MapStorage storage = DimensionManager.getWorld(0).getMapStorage();
        this.data = (T)storage.getOrLoadData(clazz, identifier);
        if(this.data == null) {
            try {
                data = clazz.getConstructor(String.class).newInstance(identifier);
                storage.setData(identifier, data);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public void markDirty() {
        if(this.data != null) {
            this.data.markDirty();
        }
    }

}
