package brokenlib.client.notification.test;

import brokenlib.BrokenLib;
import brokenlib.client.notification.NotificationDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class TestNotificationDisplay extends NotificationDisplay {

    private int id;

    public TestNotificationDisplay(int id) {
        this.id = id;
    }

    @Override
    public void drawNotification(int mouseX, int mouseY, int x, int y, int width, boolean hovered) {
        Gui.drawRect(x, y, x + width, y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 4, 0xFFFF5555);
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3, 0xFFFFAAAA);
        Minecraft.getMinecraft().fontRenderer.drawString("Click on me", x + 2, y + 3, 0xFFFFFF);
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        BrokenLib.proxy.getNotificationManager().replyToServer(id);
    }

    @Override
    public int getHeightFromWidth(int width) {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 4;
    }

    @Override
    public int getNotificationId() {
        return id;
    }
}
