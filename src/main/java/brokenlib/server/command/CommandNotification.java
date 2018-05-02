package brokenlib.server.command;

import brokenlib.server.command.notification.CommandNotificationList;
import brokenlib.server.command.notification.CommandNotificationRemove;
import brokenlib.server.command.notification.CommandNotificationTest;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.Optional;

public class CommandNotification extends CommandTreeBase {

    public CommandNotification() {
        this.addSubcommand(new CommandNotificationList());
        this.addSubcommand(new CommandNotificationTest());
        this.addSubcommand(new CommandNotificationRemove());
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        Optional<Boolean> result = this.getSubCommands().stream().map(c -> c.checkPermission(server, sender)).reduce(Boolean::logicalOr);
        return result.isPresent() && result.get();
    }

    @Override
    public String getName() {
        return "notification";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.brokenlib.notification";
    }
}
