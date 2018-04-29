package brokenlib.server.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.PermissionAPI;

public abstract class CommandPermissionBased extends CommandBase {

    public abstract String getPermissionNode();

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if(sender instanceof EntityPlayer)
            return PermissionAPI.hasPermission((EntityPlayer)sender, getPermissionNode());
        return super.checkPermission(server, sender);
    }

}
