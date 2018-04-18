package brokenlib.common.notification;

import brokenlib.BrokenLib;
import brokenlib.common.network.BrokenLibNetwork;
import brokenlib.common.notification.packet.CSPacketNotification;
import brokenlib.common.notification.packet.SPacketRemoveNotification;
import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class NotificationManager {

    private static volatile NotificationManager instance = null;

    public static NotificationManager instance() {
        if (instance == null) {
            synchronized (NotificationManager.class) {
                if (instance == null) {
                    instance = new NotificationManager();
                }
            }
        }
        return instance;
    }

    private BrokenLibNetwork notificationNetwork;
    private HashMap<String, WeakReference<DedicatedNotificationManager>> queriedManagers;

    private NotificationManager() {
        notificationNetwork = BrokenLib.newNetwork(BrokenLib.MODID + "_notification_system");
        notificationNetwork.registerPacket(CSPacketNotification.class);
        notificationNetwork.registerPacket(SPacketRemoveNotification.class);

        queriedManagers = new HashMap<>();
    }

    BrokenLibNetwork getNetwork() {
        return this.notificationNetwork;
    }

    /**
     * Creates a manager for the notification system which allows you to send notifications to the clients. The manager
     * has to be queried during {@link LoaderState#INITIALIZATION} and only once. You can call this on both side but
     * it only be useful on server side.
     * @return a dedicated manager for your mod
     */
    public DedicatedNotificationManager queryDedicatedManager() {
        Preconditions.checkState(
                Loader.instance().getLoaderState().ordinal() == LoaderState.INITIALIZATION.ordinal(),
                "The manager should be queried in initialization state."
        );

        String modId = Loader.instance().activeModContainer().getModId();

        Preconditions.checkState(!this.queriedManagers.containsKey(modId),
                "The manager should be queried only once.");

        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if(side.isServer())
            ServerNotificationManager.instance(); // Init the manager (loads notifications, etc ...)
        DedicatedNotificationManager manager =  new DedicatedNotificationManager(modId, side);
        this.queriedManagers.put(modId, new WeakReference<>(manager));
        return manager;
    }

    /**
     * This method is here for internal use, don't call it.
     */
    public void receivedPacketFromClient(CSPacketNotification pck, EntityPlayerMP player) {
        ServerNotificationManager.instance().receivedNotificationPacket(player, pck);
    }

    /**
     * This method is here for internal use, don't call it.
     */
    public void receivedPacketFromServer(CSPacketNotification pck)  {
        ClientNotificationManager.instance().receivedNotificationPacket(pck);
    }

}
