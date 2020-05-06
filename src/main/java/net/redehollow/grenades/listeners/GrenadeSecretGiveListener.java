package net.redehollow.grenades.listeners;

import net.redehollow.grenades.grenade.enums.GrenadeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author oNospher
 **/
public class GrenadeSecretGiveListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if(message.toLowerCase().startsWith("/grenades")) {
            if(!player.isOp()) return;
            event.setCancelled(true);
            for(GrenadeType grenadeType : GrenadeType.values()) {
                player.getInventory().addItem(grenadeType.getItem().build());
            }
        }
    }

}
