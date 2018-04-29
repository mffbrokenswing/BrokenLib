package brokenlib.server.command.notification;

import brokenlib.BrokenLib;
import brokenlib.common.notification.test.NotificationTest;
import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.common.utils.MessageBuilder;
import brokenlib.server.command.CommandPermissionBased;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandNotificationTest extends CommandPermissionBased {

    @Override
    public String getPermissionNode() {
        return BrokenLibPerms.NOTIFICATION_TEST.getNode();
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.notification.test";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1)
            throw new WrongUsageException(getUsage(sender));

        EntityPlayer player = getPlayer(server, sender, args[0]);
        NotificationTest n = new NotificationTest();
        n.setTester(getCommandSenderAsPlayer(sender).getUniqueID());
        BrokenLib.proxy.getNotificationManager().sendNotificationTo(player.getUniqueID(), n);
        sender.sendMessage(MessageBuilder.build("${green}A test notification was sent to ${aqua}{}", player.getName()));
    }
}
