package brokenlib.server.command.notification.list;

import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.server.command.CommandPermissionBased;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class CommandNotificationListPlayer extends CommandPermissionBased {

    @Override
    public String getPermissionNode() {
        return BrokenLibPerms.NOTIFICATION_LIST_PLAYER.getNode();
    }

    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.notification.list.player";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1)
            throw new WrongUsageException(getUsage(sender));

        String playerName = args[0];
        GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(playerName);
        CommandNotificationListFilter lister = new CommandNotificationListFilter(
                getPermissionNode(),
                getName(),
                getUsage(null),
                n -> profile == null ? n.getTarget().toString().equals(playerName) : n.getTarget().equals(profile.getId())
        );
        String[] shiftedArgs = Arrays.copyOf(args, args.length - 1);
        lister.execute(server, sender, shiftedArgs);
    }
}
