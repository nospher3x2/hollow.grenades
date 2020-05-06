package net.redehollow.grenades.listeners;

import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.enums.GrenadeType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author oNospher
 **/
public class GrenadeInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Action action = event.getAction();

        if(action.name().contains("CLICK")) {
            ItemStack itemStack = player.getItemInHand();

            if(itemStack == null) return;

            GrenadeType grenadeType = GrenadeType.getGrenade(itemStack);

            if(grenadeType == null) return;

            ItemStack grenade = grenadeType.getItem().build();

            if(!itemStack.isSimilar(grenade)) return;

            if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount()-1);
            else player.setItemInHand(null);

            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);

            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setMetadata(
                    grenadeType == GrenadeType.DISTRACTION ?
                            HollowGrenades.DISTRACTION_GRENADE_METADATA :
                            HollowGrenades.EXPLOSIVE_GRENADE_METADATA,
                    new FixedMetadataValue(
                            HollowGrenades.getInstance(),
                            true
                    )
            );

            snowball.setMetadata(
                    "shooter",
                    new FixedMetadataValue(
                            HollowGrenades.getInstance(),
                            player.getName()
                    )
            );
        }
    }
}
