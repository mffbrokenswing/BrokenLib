package brokenlib.server.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandBrokenLib extends CommandTreeBase {

    public CommandBrokenLib() {
        this.addSubcommand(new CommandVersion());
    }

    @Override
    public String getName() {
        return "brokenlib";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }
}
