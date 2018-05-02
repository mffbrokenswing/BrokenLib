package brokenlib.server.command.notification.list;

import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.server.command.CommandPermissionBased;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class CommandNotificationListMod extends CommandPermissionBased {

    @Override
    public String getPermissionNode() {
        return BrokenLibPerms.NOTIFICATION_LIST_MOD.getNode();
    }

    @Override
    public String getName() {
        return "mod";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.notification.list.mod.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1)
            throw new WrongUsageException(getUsage(sender));

        String modid = args[0];
        CommandNotificationListFilter lister = new CommandNotificationListFilter(
                getPermissionNode(),
                getName(),
                getUsage(null),
                n -> n.getNotification().getEmitter().get().equals(modid)
        );
        String[] shiftedArgs = Arrays.copyOf(args, args.length - 1);
        lister.execute(server, sender, shiftedArgs);
    }
}
