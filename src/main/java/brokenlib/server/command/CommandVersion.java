package brokenlib.server.command;

import brokenlib.BrokenLib;
import brokenlib.common.permissions.BrokenLibPerms;
import brokenlib.common.utils.MessageBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.Optional;

public class CommandVersion extends CommandPermissionBased {

    @Override
    public String getPermissionNode() {
        return BrokenLibPerms.VERSION.getNode();
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.brokenlib.version.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Optional<String> version = Loader.instance().getActiveModList().stream()
                .filter(m -> m.getModId().equals(BrokenLib.MODID))
                .map(ModContainer::getVersion)
                .findFirst();
        sender.sendMessage(MessageBuilder.build("${gold}BrokenLib version : ${aqua}{}", version.get()));
    }

}
