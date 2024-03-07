package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class createbank implements CommandExecutor {
    Database database = new Database();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("bankcreate")){
            // /bankcreate [ユーザー名]

            Bukkit.getLogger().info("/bankcreate実行");
            Player commandsender = (Player)sender;

            if(args.length != 1){
                commandsender.sendMessage(ChatColor.RED + "[edamameBank] "+
                        ChatColor.YELLOW + ChatColor.BOLD+"/bankcreate [ユーザー名]" +
                        ChatColor.WHITE + ChatColor.BOLD + "の形で入力してください");
                Bukkit.getLogger().warning("/bankcreate構文エラー");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if(player != null){
                database.AddPlayerData(player, commandsender);
                return true;
            }else {
                commandsender.sendMessage(ChatColor.RED + "[edamameBank] " +
                        ChatColor.YELLOW + ChatColor.BOLD + args[0] +
                        ChatColor.WHITE + ChatColor.BOLD + "は現在オフラインです");
                Bukkit.getLogger().warning("/bankcreate名前エラー");
                return false;
            }
        }

        return false;
    }
}
