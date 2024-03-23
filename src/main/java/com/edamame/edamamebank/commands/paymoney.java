package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class paymoney implements CommandExecutor {
    Database database = new Database();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("pay")){
            Player sender = (Player) commandSender;

            if(!sender.hasPermission("edamameBank.pay")){
                sender.sendMessage(ChatColor.RED + "[edamameBank error] "+
                        ChatColor.WHITE + ChatColor.BOLD + "このコマンドを実行する権限がありません");
                return true;
            }

            if(args.length != 2){
                sender.sendMessage(ChatColor.RED + "[edamameBank error] " +
                                ChatColor.YELLOW + ChatColor.BOLD + "/pay [ユーザー名] [値]" +
                                ChatColor.WHITE + ChatColor.BOLD + "の形で入力してください");
                Bukkit.getLogger().warning("/pay構文エラー");
                return false;
            }

            Player receiver = Bukkit.getPlayer(args[0]);
            int money = Integer.parseInt(args[1]);

            if(receiver == null){
                sender.sendMessage(ChatColor.RED + "[edamameBank error] " +
                        ChatColor.YELLOW + ChatColor.BOLD + args[0] +
                        ChatColor.WHITE + ChatColor.BOLD + "は現在オフラインです");
                Bukkit.getLogger().warning("/pay名前エラー");
                return false;
            } else if (money <= 0) {
                sender.sendMessage(ChatColor.RED + "[edamameBank error] " +
                        ChatColor.WHITE + ChatColor.BOLD + "payする金額は1円以上でないといけません");
                Bukkit.getLogger().warning("/pay金額エラー");
                return false;
            } else {
                database.PayMoney(sender, receiver, money);
                return true;
            }


        }

        return false;
    }
}
