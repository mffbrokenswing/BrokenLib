package brokenlib.common.notification;

import brokenlib.client.notification.NotificationDisplay;
import brokenlib.common.network.BrokenLibNetwork;
import brokenlib.common.notification.packet.CSPacketNotification;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.LinkedList;

public class ClientNotificationManager {

    private static volatile ClientNotificationManager instance = null;

    public static ClientNotificationManager instance() {
        if (instance == null) {
            synchronized (ClientNotificationManager.class) {
                if (instance == null)
                    instance = new ClientNotificationManager();
            }
        }
        return instance;
    }

    private HashMap<Integer, Notification> notifications;
    private LinkedList<NotificationDisplay> displays;
    private final BrokenLibNetwork network;

    private ClientNotificationManager() {
        this.notifications = new HashMap<>();
        this.network = NotificationManager.instance().getNetwork();
        this.displays = new LinkedList<>();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.notifications.clear();
        this.displays.clear();
    }

    public void receivedNotificationPacket(CSPacketNotification pck) {
        try {
            Class<?> c = Class.forName(pck.getNotificationClass());
            if (Notification.class.isAssignableFrom(c)) {

                // DATA

                Notification notification = (Notification) c.getConstructor().newInstance();
                notification.fromNBT(pck.getNBT(), Side.CLIENT);
                this.notifications.put(pck.getNotificationId(), notification);
                notification.receivedOnClient();

                // DISPLAY

                updateDisplays();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<NotificationDisplay> getDisplays() {
        return displays;
    }

    void sendReplyToServer(int id, String modId) {
        Notification notification = this.notifications.get(id);
        if(notification != null && notification.getEmitter().get().equals(modId)) {
            new CSPacketNotification(id, notification, Side.CLIENT).sendUsing(this.network).toServer();
        }
    }

    public void removeNotification(int id) {
        if(this.notifications.containsKey(id)) {
            this.notifications.remove(id).onRemove(Side.CLIENT);
            updateDisplays();
        }
    }

    void updateDisplays() {
        this.displays.clear();
        this.notifications.entrySet().stream()
                .map(entry ->
                        Pair.of(entry.getValue().getTimestamp().get(),
                                entry.getValue().getDisplay(entry.getKey()))
                )
                .sorted((p1, p2) -> Long.compare(p1.getKey(), p1.getKey()))
                .map(Pair::getValue)
                .forEachOrdered(this.displays::addLast);
    }

}
