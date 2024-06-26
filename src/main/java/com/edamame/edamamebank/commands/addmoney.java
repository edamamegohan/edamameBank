package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addmoney implements CommandExecutor {
    Database database = new Database();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("addmoney")){
            // /addmoney [ユーザー名] [値]

            Player commandsender = (Player)sender;

            if(!commandsender.hasPermission("edamameBank.addmoney")){
                commandsender.sendMessage(ChatColor.RED + "[edamameBank error] "+
                        ChatColor.WHITE + ChatColor.BOLD + "このコマンドを実行する権限がありません");
                return true;
            }

            Bukkit.getLogger().info("/addmoney実行");

            if(args.length != 2){
                commandsender.sendMessage(ChatColor.RED + "[edamameBank error] "+
                        ChatColor.YELLOW + ChatColor.BOLD+"/addmoney [ユーザー名] [値]" +
                        ChatColor.WHITE + ChatColor.BOLD + "の形で入力してください");
                Bukkit.getLogger().warning("/addmoney構文エラー");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if(player != null){
                database.AddMoney(player, Integer.parseInt(args[1]));
                sender.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                        ChatColor.YELLOW + ChatColor.BOLD + player.getDisplayName() +
                        ChatColor.WHITE + ChatColor.BOLD + "のお金を" +
                        ChatColor.YELLOW + ChatColor.BOLD + Integer.parseInt(args[1]) +
                        ChatColor.WHITE + ChatColor.BOLD + "円追加しました");
                return true;
            }else {
                commandsender.sendMessage(ChatColor.RED + "[edamameBank] " +
                        ChatColor.YELLOW + ChatColor.BOLD + args[0] +
                        ChatColor.WHITE + ChatColor.BOLD + "は現在オフラインです");
                Bukkit.getLogger().warning("/addmoney名前エラー");
                return false;
            }
        }

        return false;
    }
}
