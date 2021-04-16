package de.legoshi.speedrunplugin.listener;

import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.player.PlayerManager;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class BlockBreakListener implements Listener {

    private AsyncMySQL mySQL;
    private PlayerManager playerManager;

    public BlockBreakListener(AsyncMySQL mySQL, PlayerManager playerManager) {
        this.mySQL = mySQL;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if(!event.getBlock().getType().equals(Material.GOLD_PLATE)) return;
        if(!event.getPlayer().isOp()) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        Location cp = event.getBlock().getLocation();
        playerManager.getHashMap().get(player).setOnDelete(cp);

        event.setCancelled(true);

        mySQL.query("SELECT * FROM maps WHERE x = " + cp.getX() + " AND y = " + cp.getY() + " AND z = " + cp.getZ() + " AND world = '" + cp.getWorld().getName() + "';", new Consumer<ResultSet>() {
            @Override
            public void accept(ResultSet resultSet) {
                try {
                    if(resultSet.next()) {
                        String mapname = resultSet.getString("mapname");
                        int id = resultSet.getInt("mapid");
                        player.sendMessage(Message.MSG_MAP_DELETE.getMessage()
                        .replace("{mapname}", mapname)
                        .replace("{id}", "" + id));
                    } else {
                        player.sendMessage(Message.ERR_MAP_DOESNT_EXIST.getErrMessage());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
