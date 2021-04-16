package de.legoshi.speedrunplugin.commands;

import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Locale;

public class MapTop implements CommandExecutor {

    private AsyncMySQL mySQL;

    public MapTop(AsyncMySQL mySQL) {
        this.mySQL = mySQL;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Message.ERR_NOT_A_PLAYER.getErrMessage());
            return false;
        }

        Player player = (Player) sender;
        int id;

        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.ERR_NOT_A_NUMBER.getErrMessage());
            return false;
        }

        mySQL.query("SELECT * FROM playertimes, maps " +
            "WHERE playertimes.mapid = maps.mapid AND playertimes.mapid = "+ id +" ORDER BY playertime ASC LIMIT 10;", resultSet -> {
                try {
                    int count = 1;
                while(resultSet.next()) {
                    if(count == 1) player.sendMessage(Message.MSG_LEADERBOARD.getMessage().replace("{mapname}", resultSet.getString("mapname")));
                    player.sendMessage(Message.MSG_LEADERBOARD_LIST.getMessage()
                    .replace("{num}", "" + count)
                    .replace("{player}", player.getName())
                    .replace("{time}", "" + String.format(Locale.US, "%.2f", (resultSet.getLong("playertime")/1000.0)) + "s"));
                    count++;
                }
                if(count == 1) { player.sendMessage(Message.MSG_NO_PASSES.getMessage()); }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

        return false;
    }
}
