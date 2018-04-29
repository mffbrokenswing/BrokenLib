package brokenlib.server.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.Optional;

public class CommandBrokenLib extends CommandTreeBase {

    public CommandBrokenLib() {
        this.addSubcommand(new CommandVersion());
        this.addSubcommand(new CommandNotification());
    }

    @Override
    public String getName() {
        return "brokenlib";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.brokenlib.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        Optional<Boolean> result = this.getSubCommands().stream().map(c -> c.checkPermission(server, sender)).reduce(Boolean::logicalOr);
        return result.isPresent() && result.get();
    }
}
