package brokenlib.common.utils;

import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.util.HashSet;

public class ServerManager {

    private static volatile ServerManager instance = null;

    public static ServerManager instance() {
        if(instance == null) {
            synchronized (ServerManager.class) {
                if(instance == null)
                    instance = new ServerManager();
            }
        }
        return instance;
    }

    private HashSet<IServerListener> listeners;

    private ServerManager() {
        this.listeners = new HashSet<>();
    }

    public void fireServerStopping(FMLServerStoppingEvent event) {
        for(IServerListener listener : this.listeners) {
            listener.serverStopping(event);
        }
    }

    public void register(IServerListener listener) {
        this.listeners.add(listener);
    }

    public void unregister(IServerListener listener) {this.listeners.remove(listener);}

}
