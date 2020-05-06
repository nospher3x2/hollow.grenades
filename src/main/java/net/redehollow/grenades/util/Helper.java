package net.redehollow.grenades.util;

import org.bukkit.ChatColor;

public class Helper {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception ignored) { }
        return false;
    }
}
