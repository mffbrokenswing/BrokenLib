package brokenlib.common.notification;

import brokenlib.client.notification.NotificationDisplay;
import brokenlib.common.notification.parameter.NotifParameter;
import brokenlib.common.notification.parameter.SharedNotifParameter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents a notification on both client and server sides. A notification contains a list of
 * {@link NotifParameter}s that represents the information about the notification. The notification is
 * created on the server and sent to the concerned player when this one is connected, then the player send a reply to
 * the server which deletes the notification. The notifications are persistent after the server stops and restarts.
 */
public abstract class Notification {

    protected final ArrayList<NotifParameter<?, ?>> parameters;
    private final NotifParameter<Long, NBTTagLong> timestamp;
    private final NotifParameter<String, NBTTagString> emitter;
    @SideOnly(Side.CLIENT)
    private NotificationDisplay cachedDisplay = null;

    public Notification() {
        this.parameters = new ArrayList<>();
        this.timestamp = new SharedNotifParameter<>("timestamp", NBTTagLong::new, NBTTagLong::getLong);
        this.emitter = new SharedNotifParameter<>("emitter", NBTTagString::new, NBTTagString::getString);

        this.parameters.add(this.timestamp);
        this.parameters.add(this.emitter);
        this.addParameters();
    }

    /**
     * Add all the parameters to the {@link ArrayList} {@link Notification::parameters} using
     * {@link Notification::addParameter}.
     */
    protected abstract void addParameters();

    protected final void addParameter(NotifParameter parameter) {
        this.parameters.add(parameter);
    }

    /**
     * @param id The id of the notification
     * @return an instance of the class displaying this notification
     */
    @SideOnly(Side.CLIENT)
    protected abstract NotificationDisplay createDisplay(int id);

    public NotificationDisplay getDisplay(int id) {
        if(this.cachedDisplay == null)
            this.cachedDisplay = createDisplay(id);
        return this.cachedDisplay;
    }

    /**
     * @return the timestamp the notification were created for the first time
     */
    public Optional<Long> getTimestamp() {
        return this.timestamp.getValue();
    }

    void setIimestamp(Long value) {
        this.timestamp.setValue(value);
    }

    /**
     * @return the id of the mod which sent the notification
     */
    public Optional<String> getEmitter() {
        return this.emitter.getValue();
    }

    void setEmitter(String modid) {
        this.emitter.setValue(modid);
    }

    /**
     * Serializes the notifications in order to send it to the other side.
     * @param side The side this method is called on
     * @return the serialized notification
     */
    public NBTTagCompound toNBT(Side side) {
        NBTTagCompound nbt = new NBTTagCompound();
        if(side.isClient()) {
            this.parameters.stream().filter(NotifParameter::canBeOverride).forEach(param -> param.writeToNBT(nbt));
        } else {
            this.parameters.stream().filter(NotifParameter::shouldBeSent).forEach(param -> param.writeToNBT(nbt));
        }
        return nbt;
    }

    /**
     * Repopulates parameter's values using the provided {@link NBTTagCompound}.
     * If this method is called client side, only the parameters which returns true to
     * {@link NotifParameter::shouldBeSent} are repopulated with a value.
     * If the method is called server side, only the parameters which returns true to
     * {@link NotifParameter::canBeOverride} are repopulated with a value.
     * @param nbt The {@link NBTTagCompound} containing the values for the concerned parameters
     * @param side The side this method is called on
     */
    public void fromNBT(NBTTagCompound nbt, Side side) {
        if(side.isClient()) {
            parameters.stream().filter(NotifParameter::shouldBeSent).forEach(param -> param.readFromNBT(nbt));
        } else {
            parameters.stream().filter(NotifParameter::canBeOverride).forEach(param -> param.readFromNBT(nbt));
        }
    }

    /**
     * Saves the notification in order to be able to retrieve the exact same instance when calling
     * {@link Notification::fromSave}.
     * @return the serialized notification to save on the disk
     */
    NBTTagCompound toSave() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.parameters.forEach(param -> param.writeToNBT(nbt));
        return nbt;
    }

    /**
     * Retrieves the serialized notification which had been saved on the disk.
     * @param nbt the serialized notification saved on the disk
     */
    void fromSave(NBTTagCompound nbt) {
        this.parameters.forEach(param -> param.readFromNBT(nbt));
    }

    /**
     * Called when the notification is removed from the manager. This method isn't called when the notification is saved
     * on the disk.
     * @param side The side the notification is removed on
     */
    abstract void onRemove(Side side);

    /**
     * Called when the notification is received client side. At this state, the notification is already populated with
     * the values the server sent.
     */
    abstract void receivedOnClient();

    /**
     * Called when the notification is received server side. At this state, the notification is already populated with
     * the values the client sent.
     * @param playerMp The player the notification were sent to
     * @return true if the notification should be removed
     */
    abstract boolean receivedOnServer(EntityPlayerMP playerMp);

}
