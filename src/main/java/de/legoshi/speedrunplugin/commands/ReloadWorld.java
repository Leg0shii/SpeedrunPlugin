package de.legoshi.speedrunplugin.commands;

import de.legoshi.speedrunplugin.SpeedrunPlugin;
import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            SpeedrunPlugin.getInstance().worldFile();
            commandSender.sendMessage(Message.SUCC_RELOAD_MESSAGES.getMessage());
        }

        return false;
    }
}
