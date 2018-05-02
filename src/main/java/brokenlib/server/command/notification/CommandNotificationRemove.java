package brokenlib.server.command.notification;

import brokenlib.common.notification.ServerNotificationManager;
import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.common.utils.MessageBuilder;
import brokenlib.server.command.CommandPermissionBased;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandNotificationRemove extends CommandPermissionBased {

    @Override
    public String getPermissionNode() {
        return BrokenLibPerms.NOTIFICATION_REMOVE.getNode();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.notification.remove.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1)
            throw new WrongUsageException(getUsage(sender));

        int id = parseInt(args[0]);
        if(ServerNotificationManager.instance().removeNotification(id)) {
            sender.sendMessage(MessageBuilder.build("${green}The notification have been successfully removed."));
        } else {
            sender.sendMessage(MessageBuilder.build("${red}There's no notification with this id."));
        }
    }
}
