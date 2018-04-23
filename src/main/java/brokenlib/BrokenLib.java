package brokenlib;

import brokenlib.common.notification.DedicatedNotificationManager;
import brokenlib.common.notification.NotificationManager;
import brokenlib.common.save.BrokenLibData;
import brokenlib.common.save.WorldDataWrapper;
import brokenlib.server.command.CommandBrokenLib;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = BrokenLib.MODID, name = "Broken Library", version = BrokenLib.VERSION)
public class BrokenLib {

    public static final String MODID = "brokenlib";
    public static final String VERSION = "0.1.0";

    public static File configDir = null;

    public static final WorldDataWrapper<BrokenLibData> DATA = new WorldDataWrapper(BrokenLibData.class, MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), MODID);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBrokenLib());
        DATA.serverStarting();
        NotificationManager.instance().initServerManager();;
    }

}
