package net.redehollow.grenades.listeners;

import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.concurrent.TimeUnit;

/**
 * @author oNospher
 **/
public class GrenadeProjectileHitListener implements Listener {

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();

        GrenadeType grenadeType = GrenadeType.getGrenade(entity);

        if(grenadeType == null) return;
        if(!entity.hasMetadata("shooter")) return;


        Player shooter = Bukkit.getPlayer(entity.getMetadata("shooter").get(0).value().toString());
        if(shooter == null) return;

        Location location = entity.getLocation();
        Integer duration = HollowGrenades.getInstance().getConfig().getInt(
                "settings.general.distraction-time"
        );

        Grenade grenade = new Grenade(
                location,
                grenadeType,
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(duration)
        );

        grenade.spawn(shooter);

        HollowGrenades.getInstance().getGrenades().add(grenade);
    }
}
