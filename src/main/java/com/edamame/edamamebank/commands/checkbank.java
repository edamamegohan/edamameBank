package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class checkbank implements CommandExecutor {
    public Database database = new Database();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("bank")){
            Player sender = (Player) commandSender;
            database.CheckMoney(sender);
            return true;
        }

        return false;
    }
}
