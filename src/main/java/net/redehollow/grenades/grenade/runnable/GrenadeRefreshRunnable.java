package net.redehollow.grenades.grenade.runnable;

import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.grenade.enums.GrenadeType;

import java.util.Iterator;

/**
 * @author oNospher
 **/
public class GrenadeRefreshRunnable implements Runnable {

    @Override
    public void run() {
        Iterator<Grenade> iterator = HollowGrenades.getInstance().getGrenades().iterator();

        while(iterator.hasNext()) {
            Grenade grenade = iterator.next();

            if (grenade.isValid()) {
                grenade.distraction();
            } else {
                grenade.remove(iterator);
            }
        }
    }
}
