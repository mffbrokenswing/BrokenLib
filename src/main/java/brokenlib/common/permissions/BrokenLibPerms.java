package brokenlib.common.permissions;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public enum BrokenLibPerms {

    NOTIFICATION_LIST_ALL("command.notification.list.all", "Shows all the notifications", DefaultPermissionLevel.OP),
    VERSION("command.version", "Displays BrokenLib's version", DefaultPermissionLevel.ALL),
    NOTIFICATION_LIST_MOD("command.notification.list.mod", "Shows all the notifications sent by a mod.", DefaultPermissionLevel.OP),
    NOTIFICATION_LIST_PLAYER("command.notification.list.player", "Shows all the notifications sent to a player", DefaultPermissionLevel.OP),
    NOTIFICATION_TEST("command.notification.test", "Sends a notification to a player", DefaultPermissionLevel.OP),
    NOTIFICATION_REMOVE("command.notification.remove", "Removes the notification with the specified id", DefaultPermissionLevel.OP);

    private String node, description;
    private DefaultPermissionLevel level;

    BrokenLibPerms(String node, String description, DefaultPermissionLevel level) {
        this.node = node;
        this.description = description;
        this.level = level;
    }

    public String getNode() {
        return node;
    }

    public String getDescription() {
        return description;
    }

    public DefaultPermissionLevel getLevel() {
        return level;
    }

    private static boolean registered = false;

    public static void registerPermissions() {
        if(!registered && Loader.instance().getLoaderState().ordinal() > LoaderState.PREINITIALIZATION.ordinal()) {
            registered = true;
            for(BrokenLibPerms p  : BrokenLibPerms.values()) {
                PermissionAPI.registerNode(p.node, p.level, p.description);
            }
        }
    }
}
