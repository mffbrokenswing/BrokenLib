package brokenlib.server.command.notification;

import brokenlib.BrokenLib;
import brokenlib.common.notification.test.NotificationTest;
import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.common.utils.MessageBuilder;
import brokenlib.server.command.CommandPermissionBased;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTP;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return "command.notification.test.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Set<EntityPlayer> targets = new HashSet<>();
        if(args.length == 0) {
            targets.add(getCommandSenderAsPlayer(sender));
        } else {
            for(int i = 0; i < args.length; i++) {
                targets.add(getPlayer(server, sender, args[i]));
            }
        }

        for (EntityPlayer target : targets) {
            NotificationTest n = new NotificationTest();
            n.setTester(getCommandSenderAsPlayer(sender).getUniqueID());
            BrokenLib.proxy.getNotificationManager().sendNotificationTo(target.getUniqueID(), n);
            sender.sendMessage(MessageBuilder.build("${green}A test notification was sent to ${aqua}{}", target.getName()));
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
