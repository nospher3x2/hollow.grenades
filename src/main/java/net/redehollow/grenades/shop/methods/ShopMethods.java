package net.redehollow.grenades.shop.methods;

import com.gabrielblink.galaxydungeons.GalaxyDungeonAPI;
import com.gabrielblink.galaxydungeons.exceptions.GalaxyUserNotFoundException;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreUpdater;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
public class ShopMethods {

    public GalaxyDungeonUser user;
    private DungeonServer dungeonServer;

    public ShopMethods(Player player) throws GalaxyUserNotFoundException {
        this.user = GalaxyDungeonAPI.getGalaxyUser(player.getName());
        this.dungeonServer = CoreStorage.getDungeonServers().get(user.getCurrentDungeon().getDungeonName());
    }

    public boolean hasNugget(double nugget) {
        return dungeonServer.getPlayers_pepitas().get(user.getPlayer()) >= nugget;
    }

    public void removeNugget(double nugget) {
        dungeonServer.getPlayers_pepitas().put(user.getPlayer(), (int) (dungeonServer.getPlayers_pepitas().get(user.getPlayer()) - nugget));
        ScoreUpdater.updateScoreBoard(Bukkit.getPlayer(user.getPlayer()), dungeonServer);
    }

}
