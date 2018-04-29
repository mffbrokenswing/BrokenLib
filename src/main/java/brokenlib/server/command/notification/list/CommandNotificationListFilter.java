package brokenlib.server.command.notification.list;

import brokenlib.common.notification.LinkedNotification;
import brokenlib.common.notification.ServerNotificationManager;
import brokenlib.common.utils.MessageBuilder;
import brokenlib.server.command.CommandPermissionBased;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandNotificationListFilter extends CommandPermissionBased {

    private static final int NOTIFICATIONS_PER_PAGE = 5;

    private String permissionNode, usage, name;
    private Function<LinkedNotification, Boolean> filter;

    public CommandNotificationListFilter(String permissionNode, String usage, String name, Function<LinkedNotification, Boolean> filter) {
        this.permissionNode = permissionNode;
        this.usage = usage;
        this.name = name;
        this.filter = filter;
    }

    @Override
    public String getPermissionNode() {
        return this.permissionNode;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return this.usage;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Map<Integer, LinkedNotification> notifications = ServerNotificationManager.instance().getNotifications()
                .entrySet()
                .stream()
                .filter(e -> this.filter.apply(e.getValue()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        if(notifications.isEmpty()) {
            sender.sendMessage(MessageBuilder.build("${green}There's no notifications."));
            return;
        }

        int maxPage = notifications.size() / NOTIFICATIONS_PER_PAGE + (notifications.size() %  NOTIFICATIONS_PER_PAGE > 0 ? 1 : 0);
        int pageNumber = args.length > 0 ? parseInt(args[0]) : 1;

        if(pageNumber < 1 || pageNumber > maxPage) {
            sender.sendMessage(MessageBuilder.build("${red}The page number {} is invalid, only pages from 1 to {} are correct.", pageNumber, maxPage));
            return;
        }

        sender.sendMessage(MessageBuilder.build("${gold}Notifications : page ${aqua}{}${gold}/${aqua}{}", pageNumber, maxPage));
        int current = 0;
        Iterator<Map.Entry<Integer, LinkedNotification>> it = notifications.entrySet().iterator();
        while(it.hasNext() && current < NOTIFICATIONS_PER_PAGE * pageNumber) {
            Map.Entry<Integer, LinkedNotification> entry = it.next();

            if(current++ < (pageNumber - 1) * NOTIFICATIONS_PER_PAGE)
                continue;

            String emitter = entry.getValue().getNotification().getEmitter().get();
            long timestamp = entry.getValue().getNotification().getTimestamp().get();
            UUID target = entry.getValue().getTarget();
            int id = entry.getKey();
            GameProfile profile = server.getPlayerProfileCache().getProfileByUUID(target);

            sender.sendMessage(
                    MessageBuilder.build(
                            "${aqua}Target : ${green}{}" +
                                    "${aqua}, Mod : ${green}{}" +
                                    "${aqua}, ID : ${green}{}" +
                                    "${aqua}, Timestamp : ${green}{}",
                            profile == null ? target : profile.getName(),
                            emitter,
                            id,
                            new Date(timestamp)
                    )
            );
        }

    }
}
