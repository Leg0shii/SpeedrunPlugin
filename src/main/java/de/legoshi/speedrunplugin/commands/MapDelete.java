package de.legoshi.speedrunplugin.commands;

import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.player.PlayerManager;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class MapDelete implements CommandExecutor {

    AsyncMySQL mySQL;
    PlayerManager playerManager;

    public MapDelete(AsyncMySQL mySQL, PlayerManager playerManager) {
        this.mySQL = mySQL;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Message.ERR_NOT_A_PLAYER.getErrMessage());
            return false;
        }

        Player player = (Player) sender;

        if(!player.isOp()) {
            player.sendMessage(Message.ERR_NOT_OP.getErrMessage());
            return false;
        }

        Location cp = playerManager.getHashMap().get(player).getOnDelete();

        if(cp != null) mapDelete(player, cp);
        else player.sendMessage(Message.ERR_NO_DELETE_SELEC.getErrMessage());

        return false;
    }

    private void mapDelete(Player player, Location cp) {

        mySQL.query("SELECT mapid FROM maps WHERE x = " + cp.getX() + " AND y = " + cp.getY() + " AND z = " + cp.getZ() + " AND world = '" + cp.getWorld().getName() + "';", resultSet -> {
            try {
                if (resultSet.next()) {
                    int mapid = resultSet.getInt("mapid");
                    mySQL.update("DELETE FROM maps WHERE mapid = " + mapid + ";");
                    mySQL.update("DELETE FROM playertimes WHERE mapid = " + mapid + ";");
                    cp.getBlock().setType(Material.AIR);
                    player.sendMessage(Message.SUCC_MAP_DELETE.getMessage().replace("{id}", "" + mapid));
                } else {
                    player.sendMessage(Message.ERR_MAP_DOESNT_EXIST.getErrMessage());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

}
