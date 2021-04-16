package de.legoshi.speedrunplugin.db;

import de.legoshi.speedrunplugin.SpeedrunPlugin;
import de.legoshi.speedrunplugin.utils.FW;
import org.bukkit.Bukkit;

import java.sql.SQLException;

public class DBManager {

    public AsyncMySQL mySQL;

    public AsyncMySQL initializeTables() {

        mySQL = connectToDB();
        if(mySQL != null) {

            Bukkit.getConsoleSender().sendMessage("DB Connected");
            mySQL.update("CREATE TABLE IF NOT EXISTS maps (mapid INT NOT NULL AUTO_INCREMENT, x DOUBLE(12,4), y DOUBLE(12,4), z DOUBLE(12,4), world VARCHAR(255), mapname VARCHAR(255), PRIMARY KEY(mapid));");
            mySQL.update("CREATE TABLE IF NOT EXISTS playertimes (playid INT NOT NULL AUTO_INCREMENT, playerUUID VARCHAR(255), mapid INT, playertime LONG, PRIMARY KEY(playid));");

        }

        return mySQL;

    }

    public AsyncMySQL connectToDB() {

        SpeedrunPlugin instance = SpeedrunPlugin.getInstance();
        FW config = new FW("./plugins/configuration/", "dbconfig.yaml");
        String host = config.getString("host");
        int port = config.getInt("port");
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");

        try {
            mySQL = new AsyncMySQL(
                instance,
                host,
                port,
                username,
                password,
                database);
            return mySQL;
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return null;

    }

}
