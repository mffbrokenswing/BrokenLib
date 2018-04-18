package brokenlib.common.utils;

import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public interface IServerListener {

    void serverStopping(FMLServerStoppingEvent event);

}
