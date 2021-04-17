package de.legoshi.speedrunplugin.listener;

import de.legoshi.speedrunplugin.SpeedrunPlugin;
import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.SQLException;

public class PlayerPlaceListener implements Listener {

    public AsyncMySQL mySQL;

    public PlayerPlaceListener(AsyncMySQL mySQL) {
        this.mySQL = mySQL;
    }

    @EventHandler
    public void onPlatePlace(BlockPlaceEvent event) {

        if(!event.getPlayer().isOp()) return;
        if(!event.getBlockPlaced().getType().equals(SpeedrunPlugin.getInstance().goal)) return;

        Player player = event.getPlayer();

        player.sendMessage(Message.MSG_INIT_MAP.getMessage());
        mapCreate(player, event.getBlockPlaced().getLocation());
    }

    public void mapCreate(Player player, Location cp) {

        mySQL.update("INSERT INTO maps (x, y, z, world, mapname) VALUES" +
            " (" + cp.getX() + "," + cp.getY() + "," + cp.getZ() + ",'"+cp.getWorld().getName() +"','newmap');");
        player.sendMessage(Message.SUCC_MAP_TO_DB.getMessage());

        mySQL.query("SELECT mapid FROM maps WHERE x = " + cp.getX() + " AND y = " + cp.getY() + " AND z = " + cp.getZ() +" AND world = '" + cp.getWorld().getName() + "';", resultSet -> {
            try {
                if (resultSet.next()) player.sendMessage(Message.MSG_CHANGE_MAPNAME.getErrMessage().replace("{id}", "" + resultSet.getInt("mapid")));
                else player.sendMessage(Message.ERR_OCCURED.getErrMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
