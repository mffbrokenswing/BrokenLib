package brokenlib.common.notification;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class LinkedNotification {

    UUID target;
    Notification notification;
    boolean      hasToBeRemoved;

    public LinkedNotification(UUID target, Notification notification) {
        this.target = target;
        this.notification = notification;
        this.hasToBeRemoved = false;
    }

    public LinkedNotification(NBTTagCompound nbt) {
        this.target = nbt.getUniqueId("target");
        this.hasToBeRemoved = true;

        try {
            String className = nbt.getString("notif_class");
            Class<?> c = Class.forName(className);
            if (Notification.class.isAssignableFrom(c)) {
                this.notification = (Notification) c.getConstructor().newInstance();
                this.notification.fromSave(nbt.getCompoundTag("notif"));
                this.hasToBeRemoved = false;
            }
        } catch (Exception e) {}
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setUniqueId("target", this.target);
        nbt.setString("notif_class", this.notification.getClass().getName());
        nbt.setTag("notif", this.notification.toSave());
        return nbt;
    }

    public UUID getTarget() {
        return target;
    }

    public Notification getNotification() {
        return notification;
    }
}