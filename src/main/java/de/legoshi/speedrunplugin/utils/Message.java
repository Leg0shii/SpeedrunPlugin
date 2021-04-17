package de.legoshi.speedrunplugin.utils;

import org.bukkit.ChatColor;

import java.util.HashMap;

public enum Message {

    Prefix,
    ErrPrefix,

    MSG_INIT_MAP,
    MSG_CHANGE_MAPNAME,
    MSG_NEW_WORLDRECORD,
    MSG_TIME_IMPROVEMENT,
    MSG_TIME,

    MSG_MAP_DELETE,
    SUCC_MAP_TO_DB,

    MSG_LEADERBOARD,
    MSG_LEADERBOARD_LIST,
    MSG_NO_PASSES,

    ERR_NOT_A_PLAYER,
    ERR_NOT_A_NUMBER,
    ERR_NOT_OP,
    ERR_NO_DELETE_SELEC,

    SUCC_MAP_DELETE,

    ERR_COMMAND_MAPCHANGE,
    SUCC_COMMAND_MAPCHANGE,

    ERR_OCCURED,
    ERR_MAP_DOESNT_EXIST,
    ERR_MAP_NOT_EXIST_REMOVE,

    SUCC_RELOAD_MESSAGES,

    TIMER

    ;

    Message m = this;
    public static HashMap<Message, String> hashMap = new HashMap<>();

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', Message.Prefix.getRawMessage() + hashMap.get(this.m));
    }
    public String getErrMessage() {
        return ChatColor.translateAlternateColorCodes('&', Message.ErrPrefix.getRawMessage() + hashMap.get(this.m));
    }
    public String getRawMessage() {
        return ChatColor.translateAlternateColorCodes('&',hashMap.get(this.m));
    }

    public static void loadMessagesIn() {
        FW messageConfig = new FW("./plugins/configuration/", "message.yaml");
        hashMap.clear();

        hashMap.put(Message.Prefix, messageConfig.getString("Prefix"));
        hashMap.put(Message.ErrPrefix, messageConfig.getString("ErrPrefix"));

        hashMap.put(Message.MSG_INIT_MAP, messageConfig.getString("MSG_INIT_MAP"));
        hashMap.put(Message.MSG_CHANGE_MAPNAME, messageConfig.getString("MSG_CHANGE_MAPNAME"));
        hashMap.put(Message.MSG_NEW_WORLDRECORD, messageConfig.getString("MSG_NEW_WORLDRECORD"));
        hashMap.put(Message.MSG_TIME_IMPROVEMENT, messageConfig.getString("MSG_TIME_IMPROVEMENT"));
        hashMap.put(Message.MSG_TIME, messageConfig.getString("MSG_TIME"));

        hashMap.put(Message.MSG_MAP_DELETE, messageConfig.getString("MSG_MAP_DELETE"));
        hashMap.put(Message.SUCC_MAP_TO_DB, messageConfig.getString("SUCC_MAP_TO_DB"));

        hashMap.put(Message.MSG_LEADERBOARD, messageConfig.getString("MSG_LEADERBOARD"));
        hashMap.put(Message.MSG_LEADERBOARD_LIST, messageConfig.getString("MSG_LEADERBOARD_LIST"));
        hashMap.put(Message.MSG_NO_PASSES, messageConfig.getString("MSG_NO_PASSES"));

        hashMap.put(Message.ERR_NOT_A_PLAYER, messageConfig.getString("ERR_NOT_A_PLAYER"));
        hashMap.put(Message.ERR_NOT_A_NUMBER, messageConfig.getString("ERR_NOT_A_NUMBER"));
        hashMap.put(Message.ERR_NOT_OP, messageConfig.getString("ERR_NOT_OP"));
        hashMap.put(Message.ERR_NO_DELETE_SELEC, messageConfig.getString("ERR_NO_DELETE_SELEC"));

        hashMap.put(Message.SUCC_MAP_DELETE, messageConfig.getString("SUCC_MAP_DELETE"));

        hashMap.put(Message.ERR_COMMAND_MAPCHANGE, messageConfig.getString("ERR_COMMAND_MAPCHANGE"));
        hashMap.put(Message.SUCC_COMMAND_MAPCHANGE, messageConfig.getString("SUCC_COMMAND_MAPCHANGE"));

        hashMap.put(Message.ERR_OCCURED, messageConfig.getString("ERR_OCCURED"));
        hashMap.put(Message.ERR_MAP_DOESNT_EXIST, messageConfig.getString("ERR_MAP_DOESNT_EXIST"));
        hashMap.put(Message.ERR_MAP_NOT_EXIST_REMOVE, messageConfig.getString("ERR_MAP_NOT_EXIST_REMOVE"));

        hashMap.put(Message.SUCC_RELOAD_MESSAGES, messageConfig.getString("SUCC_RELOAD_MESSAGES"));

        hashMap.put(Message.TIMER, messageConfig.getString("TIMER"));

    }

}
