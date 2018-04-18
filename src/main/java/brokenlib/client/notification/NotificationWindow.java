package brokenlib.client.notification;

import brokenlib.BrokenLib;
import brokenlib.common.notification.ClientNotificationManager;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = BrokenLib.MODID, value = Side.CLIENT)
public class NotificationWindow extends Gui {

    private int width, height;
    private int x, y;

    public NotificationWindow(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        int totalHeight = y;
        for(NotificationDisplay display : ClientNotificationManager.instance().getDisplays()) {
            totalHeight += display.drawNotification(mouseX, mouseY, x, totalHeight, width);
        }
    }

}

