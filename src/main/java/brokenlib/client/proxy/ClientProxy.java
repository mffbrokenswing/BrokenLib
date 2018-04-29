package brokenlib.client.proxy;

import brokenlib.BrokenLib;
import brokenlib.client.notification.GuiNotification;
import brokenlib.common.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = BrokenLib.MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final KeyBinding NOTIF_BINDING = new KeyBinding(
            "key.notification.desc",
            KeyConflictContext.IN_GAME,
            Keyboard.KEY_N,
            "BrokenLib"
    );

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientRegistry.registerKeyBinding(NOTIF_BINDING);
    }

    @SubscribeEvent
    public static void keyBindingResponse(InputEvent.KeyInputEvent event) {
        if(NOTIF_BINDING.isPressed() && NOTIF_BINDING.getKeyConflictContext().isActive()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiNotification());
        }
    }

}
