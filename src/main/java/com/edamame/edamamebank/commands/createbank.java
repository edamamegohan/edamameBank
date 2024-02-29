package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class createbank implements CommandExecutor {
    Database database = new Database();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("bank") && args[0].equalsIgnoreCase("create")){
            Bukkit.getLogger().info("/bank create実行");

            Player player = (Player)sender;
            String uuid = player.getUniqueId().toString();

            database.AddPlayerData(uuid, player);
            return true;
        }

        return false;
    }
}
