package net.redehollow.grenades.grenade.data;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import net.redehollow.grenades.util.FreezeMob;
import net.redehollow.grenades.util.inventory.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter
public class Grenade {

    private final Location location;
    private final GrenadeType grenadeType;
    private final Long duration;

    public Boolean canRemove() {
        return System.currentTimeMillis() >= this.duration;
    }

    public void spawn(Player player) {
        if (this.grenadeType == GrenadeType.DISTRACTION) {
            ArmorStand armorStand = this.location.getWorld().spawn(this.location, ArmorStand.class);

            armorStand.setBasePlate(false);
            armorStand.setArms(true);
            armorStand.setVisible(true);
            armorStand.setCanPickupItems(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);

            armorStand.setHelmet(
                    new ItemBuilder(Material.SKULL_ITEM)
                    .durability(3)
                    .owner(player.getName())
                    .build()
            );

            armorStand.setChestplate(
                    new ItemBuilder(Material.LEATHER_CHESTPLATE)
                    .color(Color.AQUA)
                    .build()
            );

            armorStand.setLeggings(
                    new ItemBuilder(Material.LEATHER_LEGGINGS)
                            .color(Color.AQUA)
                            .build()
            );

            armorStand.setBoots(
                    new ItemBuilder(Material.LEATHER_BOOTS)
                            .color(Color.AQUA)
                            .build()
            );

            armorStand.setMaxHealth(9999D);
            armorStand.setHealth(2048D);

            armorStand.setMetadata("distraction", new FixedMetadataValue(HollowGrenades.getInstance(), true));
            FreezeMob.nms(armorStand, "Invulnerable");
        }

        if(this.grenadeType == GrenadeType.EXPLOSIVE) {
            List<Entity> entities = this.getLocation().getWorld().getNearbyEntities(this.getLocation(), 7, 7, 7).stream()
                    .filter(entity -> !(entity instanceof Player))
                    .filter(entity -> !entity.isDead())
                    .collect(Collectors.toList());

            List<Location> locations = Lists.newArrayList();

            entities.forEach(entity -> locations.add(entity.getLocation()));

            locations.forEach(location1 -> {
                location1.getBlock().setType(Material.FIRE);
            });
        }
    }

}
