package net.redehollow.grenades.listeners;

import net.redehollow.grenades.grenade.enums.GrenadeType;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author oNospher
 **/
public class PlayerDamageByGrenadeListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (GrenadeType.getGrenade(entity) != null) event.setCancelled(true);
    }
}
