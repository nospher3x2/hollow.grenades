package net.redehollow.grenades.shop.inventory;

import com.gabrielblink.galaxydungeons.exceptions.GalaxyUserNotFoundException;
import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import net.redehollow.grenades.shop.methods.ShopMethods;
import net.redehollow.grenades.util.inventory.InventoryBuilder;
import net.redehollow.grenades.util.inventory.item.ItemBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author oNospher
 **/
public class ShopInventory {

    public static InventoryBuilder getInventory() {
        InventoryBuilder inventory = new InventoryBuilder(4, "Loja");

        Arrays.stream(GrenadeType.values()).forEach(grenadeType -> {
            ItemBuilder item = grenadeType.getItem();
            List<String> lore = item.build().getItemMeta().getLore();
            double price = HollowGrenades.getInstance().getConfig().getDouble("settings.price." + grenadeType.toString().toLowerCase());
            lore.add(
                    String.format(
                            "§fPreço: §e%s pepitas",
                            price
                    )
            );
            item.lore(lore);

            inventory.setItem(grenadeType.getSlot(), item.setConsumer(event -> {
                Player player = (Player) event.getWhoClicked();
                try {
                    ShopMethods method = new ShopMethods(player);
                    if(!method.hasNugget(price)) {
                        player.sendMessage("§cVocê não tem pepitas suficiente para comprar essa granada.");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                        return;
                    }
                    if(player.getInventory().firstEmpty() != 1) {
                        player.sendMessage("§cVocê não tem espaço suficiente no seu inventário para receber esta granada.");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                        return;
                    }
                    method.removeNugget(price);
                    player.getInventory().addItem(grenadeType.getItem().build());
                    player.sendMessage(
                            String.format(
                                    "§cVocê comprou a %s.",
                                    grenadeType.getDisplayName()
                            )
                    );
                } catch (GalaxyUserNotFoundException e) {
                    e.printStackTrace();
                }
            }));
        });

        inventory.setCancel(true);
        return inventory;
    }
}
