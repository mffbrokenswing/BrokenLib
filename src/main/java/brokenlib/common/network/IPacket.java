package brokenlib.common.network;

import brokenlib.BrokenLib;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

/**
 * This interface is a wrapper of the interfaces {@link IMessage} and {@link IMessageHandler}. It permits to write less
 * code when making a packet. In order to increase readability, the static import of the functions of
 * {@link PacketUtils} is encouraged.
 * @param <P> The class name of the packet itself
 */
public interface IPacket<P extends IPacket<P>> extends IMessage, IMessageHandler<P, IMessage> {

    void handle(MessageContext context);

    /**
     * Indicates if the packet should be handled on the main thread of the specified side.
     * @param side The side the packet is received on
     * @return true if the packet should be handled on the main thread
     */
    default boolean executeOnMainThread(Side side) {
        return true;
    }

    @Override
    default IMessage onMessage(P message, MessageContext ctx) {
        if(!this.executeOnMainThread(ctx.side))
            message.handle(ctx);
        else if(ctx.side.isClient()) {
            Minecraft.getMinecraft().addScheduledTask(() -> message.handle(ctx));
        } else {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> message.handle(ctx));
        }
        return null;
    }

    /**
     * @return an {@link EnumSet} containing the sides the packet should be sent to
     */
    EnumSet<Side> getTargetedSides();

    default BrokenLibNetwork.PreparedPacket sendUsing(BrokenLibNetwork network) {
        return new BrokenLibNetwork.PreparedPacket(this, network);
    }

}
