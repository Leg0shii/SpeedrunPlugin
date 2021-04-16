package de.legoshi.speedrunplugin.utils;

import org.bukkit.ChatColor;

public enum Message {

    Prefix(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + ChatColor.BOLD + "HuHu" + ChatColor.RESET + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY),
    ErrPrefix(ChatColor.DARK_RED + "[" + ChatColor.RED + ChatColor.BOLD + "HuHu" + ChatColor.RESET + ChatColor.DARK_RED + "] " + ChatColor.RED),

    MSG_INIT_MAP("Initialising new map."),
    MSG_CHANGE_MAPNAME("Change map name with /mapnamechange {id} <new name>"),
    MSG_NEW_WORLDRECORD("{player} got a new worldrecord on {mapname} with a time of {time}s!"),
    MSG_TIME_IMPROVEMENT("You got a new best time on {mapname} with a time of {time}s!"),
    MSG_TIME("You beat {mapname} with a time of {time}s!"),

    MSG_MAP_DELETE("Are you sure to delete map {mapname} with id: {id}? All player stats will be deleted! Type /delete to confirm map deletion!"),
    SUCC_MAP_TO_DB("Successfully added new map to database!"),

    MSG_LEADERBOARD("Mapleaderboard - {mapname}"),
    MSG_LEADERBOARD_LIST("{num}. {player}: {time}"),
    MSG_NO_PASSES("This map hasnt been passed yet!"),

    ERR_NOT_A_PLAYER("You are not a player!"),
    ERR_NOT_A_NUMBER("Please enter a number!"),
    ERR_NOT_OP("You are not OP!"),
    ERR_NO_DELETE_SELEC("You have no map for deletion selected"),

    SUCC_MAP_DELETE("Successfully deleted map with id: {id}!"),

    ERR_COMMAND_MAPCHANGE("Please type /mapnamechange <id> <new name>"),
    SUCC_COMMAND_MAPCHANGE("Successfully changed map mame from map with id {id} to {mapname}"),

    ERR_OCCURED("An error occured..."),
    ERR_MAP_DOESNT_EXIST("Map doesn't exist...")

    ;

    String m;

    Message(String message) {
        this.m = message;
    }

    public String getMessage() {
        return Message.Prefix.getRawMessage() + this.m;
    }
    public String getErrMessage() {
        return Message.ErrPrefix.getRawMessage() + this.m;
    }
    public String getRawMessage() {
        return this.m;
    }

}
