package brokenlib.server.command.notification;

import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.server.command.notification.list.CommandNotificationListFilter;
import brokenlib.server.command.notification.list.CommandNotificationListMod;
import brokenlib.server.command.notification.list.CommandNotificationListPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.Optional;

public class CommandNotificationList extends CommandTreeBase {

    public CommandNotificationList() {
        this.addSubcommand(new CommandNotificationListFilter(
                BrokenLibPerms.NOTIFICATION_LIST_ALL.getNode(),
                "command.notification.list.all",
                "all",
                n -> true
                ));
        this.addSubcommand(new CommandNotificationListMod());
        this.addSubcommand(new CommandNotificationListPlayer());
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.brokenlib.notification.list";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        Optional<Boolean> result = this.getSubCommands().stream().map(c -> c.checkPermission(server, sender)).reduce(Boolean::logicalOr);
        return result.isPresent() && result.get();
    }

}
