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
        if(command.getName().equalsIgnoreCase("addmoney")){
            // /addmoney [ユーザー名] [値]

            Player commandsender = (Player)sender;
            Bukkit.getLogger().info("/addmoney実行");

            if(args.length != 2){
                commandsender.sendMessage("[edamameBank] /addmoney [ユーザー名] [値]の形で入力してください");
                Bukkit.getLogger().warning("/bankcreate構文エラー");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if(player != null){
                database.AddMoney(player, Integer.parseInt(args[1]), commandsender);
                return true;
            }else {
                commandsender.sendMessage("[edamameBank] " + args[0] + "は現在オフラインです");
                Bukkit.getLogger().warning("/bankcreate名前エラー");
                return false;
            }
        }

        return false;
    }
}
