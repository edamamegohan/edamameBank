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

            if(!sender.hasPermission("edamameBank.bank")){
                sender.sendMessage(ChatColor.RED + "[edamameBank error] "+
                        ChatColor.WHITE + ChatColor.BOLD + "このコマンドを実行する権限がありません");
                return true;
            }

            int money = database.CheckMoney(sender);
            sender.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + sender.getDisplayName() +
                    ChatColor.WHITE + ChatColor.BOLD + "の現在の所持金は" +
                    ChatColor.YELLOW + ChatColor.BOLD + money +
                    ChatColor.WHITE + ChatColor.BOLD + "円です");
            return true;
        }

        return false;
    }
}
