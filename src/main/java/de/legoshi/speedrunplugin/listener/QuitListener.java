package de.legoshi.speedrunplugin.listener;

import de.legoshi.speedrunplugin.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private PlayerManager playerManager;

    public QuitListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        playerManager.getHashMap().get(player).setInCourse(false);
        playerManager.unloadPlayer(player);

    }

}
