package brokenlib.common.proxy;

import brokenlib.BrokenLib;
import brokenlib.common.notification.DedicatedNotificationManager;
import brokenlib.common.notification.NotificationManager;
import brokenlib.common.notification.ServerNotificationManager;
import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.common.save.BrokenLibData;
import brokenlib.common.save.WorldDataWrapper;
import brokenlib.server.command.CommandBrokenLib;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    private DedicatedNotificationManager manager;
    private final WorldDataWrapper<BrokenLibData> data = new WorldDataWrapper<>(BrokenLibData.class, BrokenLib.MODID);

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        BrokenLibPerms.registerPermissions();
        manager = NotificationManager.instance().queryDedicatedManager();
    }

    public DedicatedNotificationManager getNotificationManager() {
        return this.manager;
    }

    public WorldDataWrapper<BrokenLibData> getDataWrapper() {
        return data;
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBrokenLib());
        data.serverStarting();
        ServerNotificationManager.instance().initSaver();
    }

}
