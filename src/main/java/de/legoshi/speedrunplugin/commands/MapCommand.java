package de.legoshi.speedrunplugin.commands;

import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class MapCommand implements CommandExecutor {

    private AsyncMySQL mySQL;

    public MapCommand(AsyncMySQL mySQL) {
        this.mySQL = mySQL;
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

        if(args.length != 2) {
            player.sendMessage(Message.ERR_COMMAND_MAPCHANGE.getErrMessage());
            return false;
        }

        mySQL.query("SELECT * FROM maps WHERE mapid = " + args[0] + ";", resultSet -> {
            try {
                if(resultSet.next()) {
                    mySQL.update("UPDATE maps SET mapname = '" + args[1] + "';");
                    player.sendMessage(Message.SUCC_COMMAND_MAPCHANGE.getMessage()
                    .replace("{id}", "" + args[0])
                    .replace("{mapname}", args[1]));
                } else {
                    player.sendMessage(Message.ERR_MAP_DOESNT_EXIST.getErrMessage());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return false;
    }
}
