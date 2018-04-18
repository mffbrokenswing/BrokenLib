package brokenlib;

import brokenlib.common.network.BrokenLibNetwork;
import brokenlib.common.utils.ServerManager;
import brokenlib.server.command.CommandBrokenLib;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = BrokenLib.MODID, version = "Broken Library")
public class BrokenLib {

    public static final String MODID = "brokenlib";
    public static final String VERSION = "0.1.0";

    public static File configDir = null;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "brokenlib");
        if(!configDir.exists())
            if(!configDir.mkdir())
                LOGGER.fatal("The notifications won't be saved. The directory {} can't be created", configDir.getAbsolutePath());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        ServerManager.instance().fireServerStopping(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBrokenLib());
    }

    public static BrokenLibNetwork newNetwork(String name) {
        return new BrokenLibNetwork(name);
    }

}
