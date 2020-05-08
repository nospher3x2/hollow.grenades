package net.redehollow.grenades.grenade.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import net.redehollow.grenades.grenade.manager.GrenadeManager;
import net.redehollow.grenades.util.FreezeMob;

import org.bukkit.Location;

import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter
public class Grenade {

    private final Location location;
    private final GrenadeType grenadeType;
    private final Long duration;

    public Boolean isValid() {
        return this.duration >= System.currentTimeMillis();
    }

    public void spawn(Player player) {
        if (this.grenadeType == GrenadeType.DISTRACTION) {
            ArmorStand armorStand = GrenadeManager.generateArmorStand(player, this);

            armorStand.setMetadata("distraction", new FixedMetadataValue(HollowGrenades.getInstance(), true));
            FreezeMob.nms(armorStand, "Invulnerable");
        }

        if(this.grenadeType == GrenadeType.EXPLOSIVE) {
            TNTPrimed tntPrimed = this.location.getWorld().spawn(this.location, TNTPrimed.class);
            tntPrimed.setMetadata("explosive", new FixedMetadataValue(HollowGrenades.getInstance(), true));
        }
    }

    public void distraction() {
        if(this.grenadeType == GrenadeType.DISTRACTION) {
            Collection<Entity> entities = this.location.getWorld().getNearbyEntities(
                    this.location,
                    15,
                    15,
                    15
            );

            LivingEntity distraction = (LivingEntity) entities.stream()
                    .filter(entity -> entity.hasMetadata("distraction"))
                    .findFirst()
                    .orElse(null);

            entities.stream()
                    .filter(entity1 -> entity1 instanceof Creature)
                    .map(entity1 -> (Creature) entity1)
                    .forEach(creature -> {
                        if (distraction != null) {
                            creature.setTarget(distraction);
                        }
                    });
        }
    }

    public void remove(Iterator<Grenade> iterator) {

        if(this.grenadeType == GrenadeType.DISTRACTION) {
            this.location.getWorld().getNearbyEntities(this.location, 1, 3, 1).stream()
                    .filter(entity1 -> entity1.hasMetadata("distraction"))
                    .findFirst()
                    .ifPresent(Entity::remove);

            iterator.remove();
        }
    }
}
