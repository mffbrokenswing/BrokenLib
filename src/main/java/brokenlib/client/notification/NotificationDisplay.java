package brokenlib.client.notification;

import net.minecraft.client.gui.Gui;

public abstract class NotificationDisplay extends Gui {

    /**
     * Draws a GUI representing a notification. The displayed GUI has to fit the specified width, not wider,
     * not thinner. For user's comfort, the return value should be constant.
     * @param mouseX the relative mouse's x position
     * @param mouseY the relative mouse's y position
     * @param x x position of the slot
     * @param y  y position of the slot
     * @param width the width to fit
     * @param hovered is the mouse in the notification display
     */
    public abstract void drawNotification(int mouseX, int mouseY, int x, int y, int width, boolean hovered);

    public abstract void mouseClicked(int mouseX, int mouseY, int button);

    /**
     * Indicates the height of the display relatively to the given width.
     * @param width The width provided to display the notification
     * @return the height of the display
     */
    public abstract int getHeightFromWidth(int width);

    public abstract int getNotificationId();

}
