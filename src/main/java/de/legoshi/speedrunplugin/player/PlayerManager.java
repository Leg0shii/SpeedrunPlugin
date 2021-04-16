package de.legoshi.speedrunplugin.player;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager {

    public HashMap<Player, PlayerObject> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public HashMap<Player, PlayerObject> getHashMap() {
        return hashMap;
    }

    public void loadPlayer(Player player) {
        PlayerObject playerObject = new PlayerObject(player, -1, 0L, false);
        hashMap.put(player, playerObject);
    }

    public void unloadPlayer(Player player) {
        hashMap.remove(player);
    }

}
