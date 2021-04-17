package de.legoshi.speedrunplugin;

import de.legoshi.speedrunplugin.commands.*;
import de.legoshi.speedrunplugin.db.AsyncMySQL;
import de.legoshi.speedrunplugin.db.DBManager;
import de.legoshi.speedrunplugin.listener.*;
import de.legoshi.speedrunplugin.player.PlayerManager;
import de.legoshi.speedrunplugin.utils.FW;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SpeedrunPlugin extends JavaPlugin {

    public static SpeedrunPlugin instance;
    public PlayerManager playerManager;
    public DBManager dbManager;
    public AsyncMySQL mySQL;

    public final Material cp = Material.IRON_PLATE;
    public final Material goal = Material.WOOD_PLATE;
    public String worldname;

    @Override
    public void onEnable() {

        instance = this;
        dbManager = new DBManager();
        playerManager = new PlayerManager();

        mySQL = dbManager.initializeTables();
        initFW();
        worldFile();

        Message.loadMessagesIn();

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
        getCommand("reloadm").setExecutor(new ReloadMessage());
        getCommand("reloadw").setExecutor(new ReloadWorld());

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

    public void worldFile() {
        FW config = new FW("./plugins/configuration/", "world.yaml");
        if(!config.exist()) {
            config.setValue("world", "world");
            this.worldname = "world";
            config.save();
            return;
        }
        this.worldname = config.getString("world");
    }

    public static SpeedrunPlugin getInstance() {
        return instance;
    }

}
