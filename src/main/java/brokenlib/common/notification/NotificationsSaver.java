package brokenlib.common.notification;

import brokenlib.BrokenLib;
import brokenlib.common.utils.IServerListener;
import brokenlib.common.utils.ServerManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class NotificationsSaver implements IServerListener {

    private static final int SAVE_DELAY = 60; // seconds
    private static final Logger LOGGER = LogManager.getLogger(BrokenLib.MODID);

    private final File saveFile;
    private ServerNotificationManager manager;
    private boolean dirty;
    private long lastSave;

    NotificationsSaver(ServerNotificationManager manager) {
        this.manager = manager;
        this.dirty = false;
        this.lastSave = 0L;
        this.saveFile = new File(BrokenLib.configDir, "notifications.dat");;
        MinecraftForge.EVENT_BUS.register(this);
        ServerManager.instance().register(this);

        if(this.saveFile.exists()) {
            try {
                NBTTagCompound nbt = CompressedStreamTools.read(saveFile);
                manager.loadFromNBT(nbt);
                LOGGER.debug("Notifications loaded.");
            } catch (IOException e) {
                LOGGER.error("Can't read file {}.", saveFile);
                e.printStackTrace();
            }
        }

    }

    public void markDirty() {
        this.dirty = true;
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if(this.dirty && System.currentTimeMillis() - lastSave > SAVE_DELAY * 1000) {
                save();
            }
        }
    }

    private void save() {
        NBTTagCompound nbt = manager.saveToNBT();
        LOGGER.debug("Notifications saved.");
        this.dirty = false;
        this.lastSave = System.currentTimeMillis();
        try {
            CompressedStreamTools.write(nbt, saveFile);
        } catch (IOException e) {
            LOGGER.error("Can't save notifications in the file {}.", saveFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    @Override
    public void serverStopping(FMLServerStoppingEvent event) {
        save();
    }
}
