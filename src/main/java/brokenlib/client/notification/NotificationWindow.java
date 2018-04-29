package brokenlib.client.notification;

import brokenlib.client.render.utils.RenderUtils;
import brokenlib.common.notification.ClientNotificationManager;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class NotificationWindow extends Gui {

    public static final int SCROLL_BAR_WIDTH = 5;

    private int width, height;
    private int drawableWidth;
    private int x, y;

    private int scrollBarColor;

    private int yOffset;
    private int scrollBarHeight, scrollBarY;

    private int clickedY, preparedOffset;

    public NotificationWindow(int x, int y, int width, int height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        this.drawableWidth = Math.max(0, this.width - SCROLL_BAR_WIDTH);
        this.x = x;
        this.y = y;
        this.yOffset = 0;
        this.scrollBarHeight = this.scrollBarY = 0;
        this.clickedY = -1;
        this.preparedOffset = 0;
        this.scrollBarColor = 0xFFFFFFFF;
    }

    public void setScrollBarColor(int color) {
        this.scrollBarColor = color;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int getNotificationsTotalHeight() {
        return ClientNotificationManager.instance().getDisplays().stream()
                .mapToInt(d -> d.getHeightFromWidth(drawableWidth))
                .sum();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        clickedY = -1;
        if(mouseX >= x && mouseX < x + drawableWidth && mouseY >= y && mouseY < y + height) {
            int displayY = y - yOffset;

            for(NotificationDisplay display : ClientNotificationManager.instance().getDisplays()) {
                int displayHeight = display.getHeightFromWidth(this.drawableWidth);
                if(mouseY >= displayY && mouseY < displayY + displayHeight) {
                    display.mouseClicked(mouseX - x, mouseY - displayY, button);
                    break;
                }
                displayY += displayHeight;
            }
        } else if(mouseX >= x + drawableWidth && mouseX < x + width && mouseY >= scrollBarY && mouseY < scrollBarY + scrollBarHeight && button == 0) {
            clickedY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if(button == 0) {
            yOffset += preparedOffset;
            preparedOffset = 0;
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick) {
        if(clickedY >= 0 && button == 0) {
            int totalHeight = getNotificationsTotalHeight();
            int delta = mouseY - clickedY;
            int createdOffset = (int)(delta * 1.0 / height * totalHeight);
            int newYOffset = yOffset + createdOffset;
            newYOffset = Math.max(0, Math.min(newYOffset, totalHeight - height));
            preparedOffset = newYOffset - yOffset;
        }
    }

    public void draw(int mouseX, int mouseY) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissorBox(x, y, x + drawableWidth, y + height);

        int totalHeight = getNotificationsTotalHeight();

        if (totalHeight <= this.height) {
            yOffset = 0;
        } else if (-yOffset + totalHeight < height) {
            yOffset = totalHeight - height;
        }
        int yDisplay = y - (yOffset + preparedOffset);
        for (NotificationDisplay display : ClientNotificationManager.instance().getDisplays()) {
            int displayHeight = display.getHeightFromWidth(drawableWidth);
            if ((yDisplay < y + this.height && yDisplay > y) || // is notification's upper bound visible
                    (yDisplay + displayHeight > y && yDisplay + displayHeight < y + height)) { // lower bound
                boolean hovered =
                        mouseX >= this.x &&
                        mouseX < this.x + this.width - SCROLL_BAR_WIDTH &&
                        mouseY >= this.y &&
                        mouseY < this.y + this.height &&
                        mouseY >= yDisplay &&
                        mouseY < yDisplay + displayHeight;
                display.drawNotification(mouseX - this.x, mouseY - yDisplay, x, yDisplay, drawableWidth, hovered);
            }
            yDisplay += displayHeight;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.height < totalHeight) {
            scrollBarHeight = (int) (this.height * 1.0 / totalHeight * this.height);
            scrollBarY = this.y + (int) ((this.yOffset + preparedOffset) * 1.0 / totalHeight * this.height);

            Gui.drawRect(this.x + this.drawableWidth, scrollBarY, this.x + this.width, scrollBarY + scrollBarHeight, this.scrollBarColor);
        }
    }

}

