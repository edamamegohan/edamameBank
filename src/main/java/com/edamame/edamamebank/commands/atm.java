package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class atm implements CommandExecutor, Listener {
    Database database = new Database();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("atm")) {
            Player player = (Player) commandSender;

            Inventory inventory = Bukkit.createInventory(null, 9, "ATM");

            for(int i = 1; i < 8; i++){
                if(i == 7)i++;
                ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName((int)Math.pow(10, i) + "円");
                itemMeta.setCustomModelData(i);
                itemMeta.setLore(Arrays.asList("左クリックで現金を電子マネーに入金する", "右クリックで電子マネーから現金を出勤する"));
                itemStack.setItemMeta(itemMeta);

                if(i != 8){
                    inventory.setItem(i, itemStack);
                }else{
                    inventory.setItem(i-1, itemStack);
                }
            }

            player.openInventory(inventory);
        }

        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getView().getTitle().equalsIgnoreCase("ATM")){
            event.setCancelled(true);
            Bukkit.getServer().broadcastMessage("Inventory Clicked");
        }
    }
}
