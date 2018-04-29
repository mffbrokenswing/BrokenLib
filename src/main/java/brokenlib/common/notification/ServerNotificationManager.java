package brokenlib.common.notification;

import brokenlib.BrokenLib;
import brokenlib.common.network.BrokenLibNetwork;
import brokenlib.common.notification.packet.CSPacketNotification;
import brokenlib.common.notification.packet.SPacketRemoveNotification;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class ServerNotificationManager {

    private static volatile ServerNotificationManager instance = null;

    public static ServerNotificationManager instance() {
        if (instance == null) {
            synchronized (ServerNotificationManager.class) {
                if (instance == null)
                    instance = new ServerNotificationManager();
            }
        }
        return instance;
    }

    private BitSet                               ids;
    private HashMap<Integer, LinkedNotification> notifications;
    private BrokenLibNetwork network;

    private ServerNotificationManager() {
        this.ids = new BitSet();
        this.notifications = new HashMap<>();
        this.network = NotificationManager.instance().getNetwork();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void initSaver() {
        BrokenLib.proxy.getDataWrapper().getData().addSubData("notification_manager", this::saveToNBT, this::loadFromNBT);
    }

    /**
     * Adds the specified notification to the notifications stack. This will be sent to the player with the specified
     * UUID as soon as he's connected.
     * @param target The UUID of the targeted player
     * @param notification The notification to be sent to the player
     */
    public void addNotification(UUID target, Notification notification) {
        notification.setIimestamp(System.currentTimeMillis());

        LinkedNotification linkedNotification = new LinkedNotification(target, notification);
        int id = getNextAvailableId();
        notifications.put(id, linkedNotification);
        BrokenLib.proxy.getDataWrapper().markDirty();

        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(target);
        if (player != null) {
            new CSPacketNotification(id, linkedNotification.notification, Side.SERVER).sendUsing(network).to(player);
        }
    }

    /**
     * Called when the client reply is received on the server. Checks if the notification id is available, if the player
     * targeted by the notification is the same player who sent the packet.
     * @param player The player replying
     * @param pck the packet received from the client
     */
    public void receivedNotificationPacket(EntityPlayerMP player, CSPacketNotification pck) {
        if (!this.notifications.containsKey(pck.getNotificationId())) return;
        LinkedNotification linkedNotification = notifications.get(pck.getNotificationId());

        if (!linkedNotification.target.equals(player.getUniqueID())) return;
        linkedNotification.notification.fromNBT(pck.getNBT(), Side.SERVER);

        if (linkedNotification.notification.receivedOnServer(player)) {
            linkedNotification.notification.onRemove(Side.SERVER);
            this.notifications.remove(pck.getNotificationId());
            freeId(pck.getNotificationId());
            new SPacketRemoveNotification(pck.getNotificationId()).sendUsing(network).to(player);
        }

        BrokenLib.proxy.getDataWrapper().markDirty();
    }

    /**
     * Sends to the player all the notifications related to him.
     * @param player The player who requires the notifications
     */
    private void clientConnection(EntityPlayerMP player) {
        this.notifications.entrySet().stream()
                .filter(e -> e.getValue().target.equals(player.getUniqueID()))
                .forEach(e -> new CSPacketNotification(e.getKey(), e.getValue().notification, Side.SERVER)
                        .sendUsing(network).to(player)
                );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote)
            clientConnection((EntityPlayerMP) event.player);
    }

    /**
     * Used to data persistence. It saves all the notifications in a NBTTagCompound.
     * @return the serialized manager
     */
    public NBTTagCompound saveToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.notifications.values().forEach(n -> list.appendTag(n.toNBT()));
        nbt.setTag("notifications", list);
        return nbt;
    }

    /**
     * Used to data persistence. It loads all the notifications from a NBTTagCompound.
     * @param nbt The serialized manager
     */
    public void loadFromNBT(NBTTagCompound nbt) {
        this.notifications.clear();
        this.ids.clear();
        NBTTagList list = nbt.getTagList("notifications", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound n = list.getCompoundTagAt(i);
            LinkedNotification notification = new LinkedNotification(n);
            if (!notification.hasToBeRemoved)
                this.notifications.put(getNextAvailableId(), notification);
        }
    }

    private int getNextAvailableId() {
        int id = ids.nextClearBit(0);
        ids.set(id);
        return id;
    }

    private void freeId(int id) {
        ids.clear(id);
    }

    public Map<Integer, LinkedNotification> getNotifications() {
        return Collections.unmodifiableMap(this.notifications);
    }

}
