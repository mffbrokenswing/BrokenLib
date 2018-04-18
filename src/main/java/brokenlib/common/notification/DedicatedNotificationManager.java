package brokenlib.common.notification;

import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class DedicatedNotificationManager {

    private final String modId;
    private final Side side;

    DedicatedNotificationManager(String modId, Side side) {
        this.modId = modId;
        this.side = side;
    }

    /**
     * Call this to send a notification to the a client.
     * @param target The UUID of the player
     * @param notification The notification to send
     */
    public void sendNotificationTo(UUID target, Notification notification) {
        if(side.isServer()) {
            notification.setEmitter(this.modId);
            ServerNotificationManager.instance().addNotification(target, notification);
        }
    }

    /**
     * Call this to reply to the server.
     * @param notificationId The id of the notification to send back to the server
     */
    public void replyToServer(int notificationId) {
        if(this.side.isClient()) {
            ClientNotificationManager.instance().sendReplyToServer(notificationId, modId);
        }
    }

}
