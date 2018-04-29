package brokenlib.common.notification.packet;

import brokenlib.common.network.IPacket;
import brokenlib.common.notification.ClientNotificationManager;
import brokenlib.common.notification.Notification;
import brokenlib.common.notification.NotificationManager;
import brokenlib.common.notification.ServerNotificationManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

import static brokenlib.common.network.PacketUtils.*;

public class CSPacketNotification implements IPacket<CSPacketNotification> {

    private NBTTagCompound nbt;
    private int notificationId;
    private String className;

    public CSPacketNotification() {}

    public CSPacketNotification(int notificationId, Notification notification, Side side) {
        this.notificationId = notificationId;
        this.nbt = notification.toNBT(side);
        this.className = notification.getClass().getName();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = readTag(buf);
        this.notificationId = buf.readInt();
        this.className = readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeTag(buf, nbt);
        buf.writeInt(this.notificationId);
        writeString(buf, className);
    }

    @Override
    public void handle(MessageContext context) {
        if(context.side.isServer()) {
            ServerNotificationManager.instance().receivedNotificationPacket(context.getServerHandler().player, this);
        } else {
            ClientNotificationManager.instance().receivedNotificationPacket(this);
        }
    }

    @Override
    public EnumSet<Side> getTargetedSides() {
        return EnumSet.of(Side.SERVER, Side.CLIENT);
    }

    public NBTTagCompound getNBT() {
        return nbt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getNotificationClass() {
        return className;
    }

}
