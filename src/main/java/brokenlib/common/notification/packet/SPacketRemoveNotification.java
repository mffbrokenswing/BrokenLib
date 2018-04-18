package brokenlib.common.notification.packet;

import brokenlib.common.network.IPacket;
import brokenlib.common.notification.ClientNotificationManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class SPacketRemoveNotification implements IPacket<SPacketRemoveNotification> {

    private int id;

    public SPacketRemoveNotification() {}

    public SPacketRemoveNotification(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public void handle(MessageContext context) {
        ClientNotificationManager.instance().removeNotification(this.id);
    }

    @Override
    public EnumSet<Side> getTargetedSides() {
        return EnumSet.of(Side.CLIENT);
    }

}
