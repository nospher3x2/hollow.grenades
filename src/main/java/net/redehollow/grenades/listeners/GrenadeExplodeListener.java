package net.redehollow.grenades.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

/**
 * @author oNospher
 **/
public class GrenadeExplodeListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof TNTPrimed) {
            if(!entity.hasMetadata("explosive")) return;
            event.blockList().clear();
        }
    }
}
