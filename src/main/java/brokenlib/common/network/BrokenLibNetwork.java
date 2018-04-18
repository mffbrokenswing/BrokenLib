package brokenlib.common.network;

import brokenlib.BrokenLib;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrokenLibNetwork {

    private static final Logger LOGGER = LogManager.getLogger(BrokenLib.MODID);

    private SimpleNetworkWrapper wrapped;
    private int packetsCount;

    public BrokenLibNetwork(String channelName) {
        wrapped = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        packetsCount = 0;
    }

    public <A extends IPacket<A>> void registerPacket(Class<A> packetClass) {
        try {
            A packet = packetClass.newInstance();
            if(packet.getTargetedSides() == null) {
                LOGGER.error("The class {} returns null for the targeted sides, it's not a correct behavior.", packetClass.getName());
                return;
            }
            for(Side side : packet.getTargetedSides()) {
                wrapped.registerMessage(packetClass, packetClass, packetsCount++, side);
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("The args-less constructor of the class {} isn't public.", packetClass.getName());
            e.printStackTrace();
        } catch (InstantiationException e) {
            LOGGER.error("The class {} is missing an args-less constructor.", packetClass.getName());
            e.printStackTrace();
        }
    }

    public PreparedPacket send(IPacket<?> packet) {
        return new PreparedPacket(packet, this);
    }

    public SimpleNetworkWrapper getWrappedNetwork() {
        return this.wrapped;
    }

    public static class PreparedPacket {

        private IPacket<?> packet;
        private BrokenLibNetwork network;

        PreparedPacket(IPacket<?> packet, BrokenLibNetwork network) {
            this.packet = packet;
            this.network = network;
        }

        public void to(EntityPlayerMP player) {
            network.wrapped.sendTo(packet, player);
        }

        public void toDimension(int dimension) {
            network.wrapped.sendToDimension(packet, dimension);
        }

        public void toAllAround(NetworkRegistry.TargetPoint targetPoint) {
            network.wrapped.sendToAllAround(packet, targetPoint);
        }

        public void toServer() {
            network.wrapped.sendToServer(packet);
        }

    }

}
