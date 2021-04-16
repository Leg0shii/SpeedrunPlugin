package de.legoshi.speedrunplugin;

import de.legoshi.speedrunplugin.commands.MapCommand;
import de.legoshi.speedrunplugin.commands.MapDelete;
import de.legoshi.speedrunplugin.commands.MapTop;
import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.db.DBManager;
import de.legoshi.speedrunplugin.listener.*;
import de.legoshi.speedrunplugin.player.PlayerManager;
import de.legoshi.speedrunplugin.utils.FW;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SpeedrunPlugin extends JavaPlugin {

    public static SpeedrunPlugin instance;
    public PlayerManager playerManager;
    public DBManager dbManager;
    public AsyncMySQL mySQL;

    @Override
    public void onEnable() {

        instance = this;
        dbManager = new DBManager();
        playerManager = new PlayerManager();

        mySQL = dbManager.initializeTables();
        initFW();

        ListenerRegistration();
        CommandRegistration();

    }

    @Override
    public void onDisable() {

    }

    private void ListenerRegistration() {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockBreakListener(mySQL, playerManager), this);
        pm.registerEvents(new JoinListener(playerManager), this);
        pm.registerEvents(new PlayerMoveListener(mySQL, playerManager), this);
        pm.registerEvents(new QuitListener(playerManager), this);
        pm.registerEvents(new PlayerPlaceListener(mySQL), this);

    }

    private void CommandRegistration() {

        getCommand("mapnamechange").setExecutor(new MapCommand(mySQL));
        getCommand("topmap").setExecutor(new MapTop(mySQL));
        getCommand("delete").setExecutor(new MapDelete(mySQL, playerManager));

    }

    public void initFW() {
        File configFile = new File("./plugins/configuration/");
        if(configFile.mkdir()) { Bukkit.getConsoleSender().sendMessage("Created config folder!"); }
        FW config = new FW("./plugins/configuration/", "dbconfig.yaml");
        if(!config.exist()) {
            config.setValue("host", "sg01-db.cus.mc-panel.net");
            config.setValue("port", 3306);
            config.setValue("username", "db_289732");
            config.setValue("password", "pw");
            config.setValue("database", "db_289732");
            config.save();
            Bukkit.getConsoleSender().sendMessage("Created FW data!");
        }
    }

    public static SpeedrunPlugin getInstance() {
        return instance;
    }

}
