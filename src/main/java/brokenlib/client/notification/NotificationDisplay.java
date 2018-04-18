package brokenlib.client.notification;

import net.minecraft.client.gui.Gui;

public abstract class NotificationDisplay extends Gui {

    /**
     * Draws a GUI representing a notification. The displayed GUI has to fit the specified width, not wider,
     * not thinner. For user's comfort, the return value should be constant.
     * @param mouseX the mouse's x position
     * @param mouseY the mouse's y position
     * @param x x position of the slot
     * @param y  y position of the slot
     * @param width the width to fit
     * @return the height of the displayed GUI
     */
    public abstract int drawNotification(int mouseX, int mouseY, int x, int y, int width);

    public abstract int getNotificationId();

}
