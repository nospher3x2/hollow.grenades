package net.redehollow.grenades.grenade.factory;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.redehollow.grenades.grenade.data.Grenade;

import java.util.List;

/**
 * @author oNospher
 **/
public class GrenadeFactory<G extends Grenade> {

    @Getter
    private List<G> grenadesSpawned = Lists.newArrayList();

}
