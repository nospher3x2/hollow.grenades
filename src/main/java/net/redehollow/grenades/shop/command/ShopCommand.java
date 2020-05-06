package net.redehollow.grenades.shop.command;

import com.gabrielblink.galaxydungeons.GalaxyDungeonAPI;
import com.gabrielblink.galaxydungeons.exceptions.GalaxyUserNotFoundException;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.redehollow.grenades.shop.inventory.ShopInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author oNospher
 **/
public class ShopCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if(message.toLowerCase().startsWith("/dungeonshop")) {
            event.setCancelled(true);
            GalaxyDungeonUser user;
            try {
                user = GalaxyDungeonAPI.getGalaxyUser(player.getName());
            } catch (GalaxyUserNotFoundException e) {
                user = null;
                e.printStackTrace();
            }
            if(user == null) {
                player.sendMessage("§cVocê precisa estar em uma dungeon para abrir a loja de granadas.");
                return;
            }
            if(user.getCurrentDungeon() == null) {
                player.sendMessage("§cVocê precisa estar em uma dungeon para abrir a loja de granadas.");
                return;
            }
            if(CoreStorage.getDungeonServers().get(user.getCurrentDungeon().getDungeonName()) == null) {
                player.sendMessage("§cVocê precisa estar em uma dungeon para abrir a loja de granadas.");
                return;
            }
            ShopInventory.getInventory().open(player);
            player.sendMessage("§aVocê abriu a loja de granadas.");
        }
    }
}
