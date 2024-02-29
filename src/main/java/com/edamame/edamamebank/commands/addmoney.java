package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addmoney implements CommandExecutor {
    Database database = new Database();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("bank") && args[0].equalsIgnoreCase("add")){
            Player player = (Player)sender;
            Bukkit.getLogger().info("/bank add実行");

            if(args.length != 2){
                player.sendMessage("[edamameBank] 不正な値です");
                return false;
            }

            database.AddMoney(player, Integer.parseInt(args[1]));

            return true;
        }

        return false;
    }
}
