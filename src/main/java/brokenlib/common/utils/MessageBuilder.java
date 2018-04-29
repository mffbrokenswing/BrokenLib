package brokenlib.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBuilder {

    private static final Pattern COLOR_PATTERN = Pattern.compile("\\$\\{(?<color>[a-z]+)}", Pattern.CASE_INSENSITIVE);
    private static final Pattern OBJECT_PATTERN = Pattern.compile("\\{(?<index>\\d*)}");

    private MessageBuilder() {}

    public static ITextComponent build(String message, Object ... objects) {
        // Preparing objects indexing
        {
            StringBuffer buffer = new StringBuffer();
            Matcher matcher = OBJECT_PATTERN.matcher(message);
            int index = 0;
            while(matcher.find()) {
                int i = matcher.group("index").isEmpty() ? index : Integer.parseInt(matcher.group("index"));
                if(i < 0 || i >= objects.length) {
                    matcher.appendReplacement(buffer, "");
                } else {
                    matcher.appendReplacement(buffer, "{" + i + "}");
                    index = Math.min(i + 1, objects.length - 1);
                }
            }
            matcher.appendTail(buffer);
            message = buffer.toString();
        }

        Matcher matcher = COLOR_PATTERN.matcher(message);
        String[] parts = COLOR_PATTERN.split(message);
        ITextComponent component = new TextComponentString(parts[0]);
        int i = 1;

        while(matcher.find() && i < parts.length) {
            String currentText = parts[i++];
            StringBuffer buffer = new StringBuffer();
            Matcher objMatcher = OBJECT_PATTERN.matcher(currentText);
            while(objMatcher.find()) {
                String replacement = objects[Integer.parseInt(objMatcher.group("index"))].toString();
                replacement = replacement.replaceAll("(?<!\\\\)\\$", "\\\\\\$");
                try {
                    objMatcher.appendReplacement(buffer, replacement);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    return MessageBuilder.build("${dark_red}Error when building message. Please contact mod author.");
                }
            }
            objMatcher.appendTail(buffer);
            ITextComponent c = new TextComponentString(buffer.toString());
            TextFormatting color = TextFormatting.getValueByName(matcher.group("color"));
            c.setStyle(new Style().setColor(color == null ? TextFormatting.RESET : color));
            component.appendSibling(c);
        }
        return component;
    }

}
