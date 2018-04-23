package brokenlib.client.render.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    // From :
    // https://github.com/DarkStorm652/Minecraft-GUI-API/blob/master/src/org/darkstorm/minecraft/gui/util/RenderUtil.java
    public static void scissorBox(final int x, final int y, final int xend, final int yend) {
        final int width = xend - x;
        final int height = yend - y;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int factor = sr.getScaleFactor();
        final int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
        GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
    }

}
