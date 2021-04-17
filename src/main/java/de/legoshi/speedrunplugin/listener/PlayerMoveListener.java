package de.legoshi.speedrunplugin.listener;

import de.legoshi.speedrunplugin.SpeedrunPlugin;
import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.player.PlayerManager;
import de.legoshi.speedrunplugin.player.PlayerObject;
import de.legoshi.speedrunplugin.utils.HotbarMessage;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

public class PlayerMoveListener implements Listener {

    private AsyncMySQL mySQL;
    private PlayerManager playerManager;

    public PlayerMoveListener(AsyncMySQL mySQL, PlayerManager playerManager) {
        this.mySQL = mySQL;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPressureplateStep(PlayerInteractEvent event) {

        if(!event.getAction().equals(Action.PHYSICAL)) return;
        PlayerObject playerObject = playerManager.getHashMap().get(event.getPlayer());
        if(event.getClickedBlock().getType().equals(SpeedrunPlugin.getInstance().cp)) {
            startTimer(playerObject);
        }

        if(event.getClickedBlock().getType().equals(SpeedrunPlugin.getInstance().goal)) {
            if(!playerObject.isInCourse()) return;
            playerObject.setInCourse(false);
            final long timeMS = playerObject.getCourseTime();
            final String time = String.format(Locale.US, "%.2f", (timeMS/1000.0));

            Location goldLoc = event.getClickedBlock().getLocation();
            String uuid = event.getPlayer().getUniqueId().toString();
            mySQL.query("SELECT * FROM maps WHERE x = " + goldLoc.getX() + " AND y = " + goldLoc.getY() + " AND z = " + goldLoc.getZ() + " AND world = " + goldLoc.getWorld().getName() + ";", rs1 -> {
                final int mapid;
                final String mapname;
                try {
                    if(rs1.next()) {
                        mapid = rs1.getInt("mapid");
                        mapname = rs1.getString("mapname");
                    }
                    else {
                        playerObject.getPlayer().sendMessage(Message.ERR_MAP_DOESNT_EXIST.getErrMessage());
                        return;
                    }
                } catch (SQLException throwables) {
                    playerObject.getPlayer().sendMessage(Message.ERR_OCCURED.getErrMessage());
                    throwables.printStackTrace();
                    return;
                }
                Player player = playerObject.getPlayer();
                mySQL.query("SELECT * FROM playertimes WHERE mapid = " + mapid + " ORDER BY playertime ASC;", resultSet -> {
                    try {
                        if(resultSet != null) {
                            if(resultSet.next()) {
                                long worldrecord = resultSet.getLong("playertime");
                                String playeruuid = player.getUniqueId().toString();
                                do {
                                    if (playeruuid.equals(resultSet.getString("playerUUID"))) {
                                        if (timeMS < worldrecord) {
                                            Bukkit.broadcastMessage(Message.MSG_NEW_WORLDRECORD.getMessage()
                                                .replace("{player}", player.getName())
                                                .replace("{mapname}", mapname)
                                                .replace("{time}", "" + time));
                                            mySQL.update("UPDATE playertimes SET playertime = " + timeMS + " WHERE playerUUID = '" + uuid + "' AND mapid = " + mapid + ";");
                                            return;
                                        }
                                        if (timeMS < resultSet.getLong("playertime")) {
                                            player.sendMessage(Message.MSG_TIME_IMPROVEMENT.getMessage()
                                                .replace("{mapname}", mapname)
                                                .replace("{time}", "" + time));
                                            mySQL.update("UPDATE playertimes SET playertime = " + timeMS + " WHERE playerUUID = '" + uuid + "' AND mapid = " + mapid + ";");
                                            return;
                                        } else {
                                            player.sendMessage(Message.MSG_TIME.getMessage()
                                                .replace("{mapname}", mapname)
                                                .replace("{time}", "" + time));
                                            return;
                                        }
                                    } else {
                                        mySQL.update("INSERT INTO playertimes (mapid, playerUUID, playertime) VALUES (" + mapid + ",'" + uuid + "'," + timeMS + ");");
                                        player.sendMessage(Message.MSG_TIME_IMPROVEMENT.getMessage()
                                            .replace("{mapname}", mapname)
                                            .replace("{time}", "" + time));
                                    }
                                } while (resultSet.next());
                            } else {
                                Bukkit.broadcastMessage(Message.MSG_NEW_WORLDRECORD.getMessage()
                                    .replace("{player}", player.getName())
                                    .replace("{mapname}", mapname)
                                    .replace("{time}", "" + time));
                                mySQL.update("INSERT INTO playertimes (mapid, playerUUID, playertime) VALUES (" + mapid + ",'" + uuid + "'," + timeMS + ");");
                            }
                        }
                    } catch (SQLException throwables) { throwables.printStackTrace(); }
                });
            });
        }
    }

    public void startTimer(PlayerObject playerObject) {
        if(!playerObject.isInCourse()) {
            playerObject.setInCourse(true);
            playerObject.setCourseTime(0);
            HotbarMessage hm = new HotbarMessage();
            new BukkitRunnable() {
                @Override
                public void run() {
                    hm.send(playerObject.getPlayer(), Message.TIMER.getRawMessage()
                        .replace("{time}", String.format(Locale.US, "%.2f", (playerObject.getCourseTime()/1000.0))));
                    if (!playerObject.isInCourse() || !playerObject.getPlayer().getLocation().getWorld().getName().equals(SpeedrunPlugin.getInstance().worldname)) {
                        cancel();
                    }
                    playerObject.setCourseTime(playerObject.getCourseTime() + 50);
                }
            }.runTaskTimer(SpeedrunPlugin.getInstance(), 0, 1);
        }
    }

}
