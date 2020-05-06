package net.redehollow.grenades;

import lombok.Getter;
import net.redehollow.grenades.grenade.data.Grenade;
import net.redehollow.grenades.grenade.factory.GrenadeFactory;
import net.redehollow.grenades.manager.StartManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author oNospher
 **/
public class HollowGrenades extends JavaPlugin {

    public static final String DISTRACTION_GRENADE_METADATA = "grenade_distraction";
    public static final String EXPLOSIVE_GRENADE_METADATA = "grenade_explosive";

    @Getter
    private static HollowGrenades instance;

    @Getter
    private GrenadeFactory<Grenade> grenadeFactory;

    @Override
    public void onEnable() {
        instance = this;
        this.grenadeFactory = new GrenadeFactory<>();
        new StartManager();
    }

    public List<Grenade> getGrenades() {
        return this.grenadeFactory.getGrenadesSpawned();
    }

}
