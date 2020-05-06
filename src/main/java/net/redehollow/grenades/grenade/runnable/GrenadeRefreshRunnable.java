package net.redehollow.grenades.grenade.runnable;

import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import org.bukkit.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author oNospher
 **/
public class GrenadeRefreshRunnable implements Runnable {

    @Override
    public void run() {
        List<Grenade> grenades = HollowGrenades.getInstance().getGrenadeFactory().getGrenadesSpawned()
                .stream()
                .filter(Objects::nonNull)
                .filter(grenade -> grenade.getGrenadeType() == GrenadeType.DISTRACTION)
                .collect(Collectors.toList());

                if(grenades.isEmpty()) return;

                grenades.forEach(grenade -> {
                    if (!grenade.canRemove()) {
                        Collection<Entity> entities = grenade.getLocation().getWorld().getNearbyEntities(grenade.getLocation(), 15, 15, 15);

                        LivingEntity distraction = (LivingEntity) entities.stream()
                                .filter(entity -> entity.hasMetadata("distraction"))
                                .findFirst()
                                .orElse(null);

                        entities.stream()
                                .filter(entity1 -> entity1 instanceof Monster)
                                .map(entity1 -> (Monster) entity1)
                                .forEach(monster -> {
                                    if (distraction != null) {
                                        monster.setTarget(distraction);
                                    }
                                });
                    } else {
                        grenade.getLocation().getWorld().getNearbyEntities(grenade.getLocation(), 1, 3, 1).stream()
                                .filter(entity1 -> entity1.hasMetadata("distraction"))
                                .findFirst()
                                .ifPresent(Entity::remove);

                        HollowGrenades.getInstance().getGrenadeFactory().getGrenadesSpawned().remove(grenade);
                    }
                });
    }
}
