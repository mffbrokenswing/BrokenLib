package brokenlib.client.notification;

import brokenlib.common.notification.Notification;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiNotification extends GuiScreen {

    private NotificationWindow window;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawRect(0, 0, 5, this.height, 0xFFAAFFAA);
        Gui.drawRect(5, 0, this.width - 5 - NotificationWindow.SCROLL_BAR_WIDTH, 5, 0xFFAAFFAA);
        Gui.drawRect(this.width - 5 - NotificationWindow.SCROLL_BAR_WIDTH, 0, this.width, this.height, 0xFFAAFFAA);
        Gui.drawRect(5, this.height - 5, this.width - 5 - NotificationWindow.SCROLL_BAR_WIDTH, this.height, 0xFFAAFFAA);
        super.drawScreen(mouseX, mouseY, partialTicks);
        window.draw(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        window.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        window.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        window.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void initGui() {
        window = new NotificationWindow(5, 5, this.width - 10, this.height - 10);
    }
}
