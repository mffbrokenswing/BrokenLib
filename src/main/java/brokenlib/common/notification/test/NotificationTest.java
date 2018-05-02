package brokenlib.common.notification.test;

import brokenlib.client.notification.NotificationDisplay;
import brokenlib.client.notification.test.TestNotificationDisplay;
import brokenlib.common.notification.Notification;
import brokenlib.common.notification.parameter.NotifParameter;
import brokenlib.common.notification.parameter.SharedNotifParameter;
import brokenlib.common.utils.MessageBuilder;
import brokenlib.common.utils.NBTUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.UUID;

public class NotificationTest extends Notification {

    private NotifParameter<UUID, NBTTagCompound> tester;

    @Override
    protected void addParameters() {
        tester = new SharedNotifParameter<>("tester", NBTUtil::createUUIDTag, NBTUtil::getUUIDFromTag);
        addParameter(tester);
    }

    public Optional<UUID> getTester() {
        return this.tester.getValue();
    }

    public void setTester(UUID uuid) {
        this.tester.setValue(uuid);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected NotificationDisplay createDisplay(int id) {
        return new TestNotificationDisplay(id);
    }

    @Override
    public void onRemove(Side side) {

    }

    @Override
    public void receivedOnClient() {

    }

    @Override
    public boolean receivedOnServer(EntityPlayerMP playerMp) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.tester.getValue().get());
        if(player != null) {
            player.sendMessage(MessageBuilder.build("${aqua}{} ${green}clicked the test notification.", playerMp.getName()));
        }
        return true;
    }
}
