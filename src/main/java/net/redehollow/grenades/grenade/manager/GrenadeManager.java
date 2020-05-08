package net.redehollow.grenades.grenade.manager;

import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.util.inventory.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

/**
 * @author oNospher
 **/
public class GrenadeManager {

    public static ArmorStand generateArmorStand(Player player, Grenade grenade) {
        ArmorStand armorStand = grenade.getLocation().getWorld().spawn(grenade.getLocation(), ArmorStand.class);

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

        return armorStand;
    }
}
