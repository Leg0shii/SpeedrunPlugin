package de.legoshi.speedrunplugin.commands;

import de.legoshi.speedrunplugin.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadMessage implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender.isOp()) {
            Message.loadMessagesIn();
            commandSender.sendMessage(Message.SUCC_RELOAD_MESSAGES.getMessage());
        }

        return false;
    }
}
