package net.redehollow.grenades.manager;

import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.runnable.GrenadeRefreshRunnable;
import net.redehollow.grenades.util.ClassGetter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * @author oNospher
 **/
public class StartManager {
    public StartManager() {
        new ListenerManager();
        HollowGrenades.getInstance().getServer().getScheduler().runTaskTimer(
                HollowGrenades.getInstance(),
                new GrenadeRefreshRunnable(),
                20L,
                0
        );
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(HollowGrenades.getInstance(), "net.redehollow").forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

                    Bukkit.getPluginManager().registerEvents(listener, HollowGrenades.getInstance());
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}