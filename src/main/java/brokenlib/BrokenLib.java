package brokenlib;

import brokenlib.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = BrokenLib.MODID, name = "Broken Library", version = BrokenLib.VERSION)
public class BrokenLib {

    public static final String MODID = "brokenlib";
    public static final String VERSION = "0.2.0-SNAPSHOT";

    @SidedProxy(clientSide = "brokenlib.client.proxy.ClientProxy", serverSide = "brokenlib.common.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

}
