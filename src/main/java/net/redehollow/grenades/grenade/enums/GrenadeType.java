package net.redehollow.grenades.grenade.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.util.inventory.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter
public enum GrenadeType {

    DISTRACTION(
            "Granada de Distração",
            new ItemBuilder(
                    Material.FIREWORK_CHARGE
            ).name(
                    "§aGranada de Distração"
            ).lore(
                    "§7Distraia todos os monstros em um raio de 15",
                    "§7blocos para onde você jogar cada granada."
            ).setFireworkColor(
                    Color.PURPLE
            ).hideAttributes(),
            15
    ),
    EXPLOSIVE(
            "Granada Explosiva",
            new ItemBuilder(
                    Material.FIREWORK_CHARGE
            ).name(
                    "§aGranada Explosiva"
            ).lore(
                    "§7A granada de explosão causa um alto dano",
                    "§7aos mobs que estão em volta dela."
            ).setFireworkColor(
                    Color.RED
            ).hideAttributes(),
            11
    );

    private final String displayName;
    private final ItemBuilder item;
    private final Integer slot;

    public static GrenadeType getGrenade(ItemStack itemStack) {
        return Arrays.stream(GrenadeType.values())
                .filter(grenadeType -> grenadeType.getItem().build().isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }

    public static GrenadeType getGrenade(Entity entity) {
        if(entity.hasMetadata(HollowGrenades.EXPLOSIVE_GRENADE_METADATA))
            return GrenadeType.EXPLOSIVE;
        else if(entity.hasMetadata(HollowGrenades.DISTRACTION_GRENADE_METADATA))
            return GrenadeType.DISTRACTION;
        return null;
    }

}
