package brokenlib.common.notification;

import java.util.UUID;

public class DedicatedNotificationManager {

    private final String modId;

    DedicatedNotificationManager(String modId) {
        this.modId = modId;
    }

    /**
     * Call this to send a notification to the client.
     * @param target The UUID of the player
     * @param notification The notification to send
     */
    public void sendNotificationTo(UUID target, Notification notification) {
            notification.setEmitter(this.modId);
            ServerNotificationManager.instance().addNotification(target, notification);
    }

    /**
     * Call this to reply to the server.
     * @param notificationId The id of the notification to send back to the server
     */
    public void replyToServer(int notificationId) {
        ClientNotificationManager.instance().sendReplyToServer(notificationId, modId);
    }

}
