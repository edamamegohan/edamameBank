package com.edamame.edamamebank.commands;

import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

            for(int i = 0; i < 8; i++){
                ItemStack itemStack;
                if(i == 0){
                    itemStack = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName("");
                    itemMeta.setLore(Arrays.asList("左クリックで現金を電子マネーに入金する", "右クリックで電子マネーから現金を出勤する"));
                    itemStack.setItemMeta(itemMeta);
                }else {
                    if(i == 7)i++;
                    itemStack = new ItemStack(Material.GOLD_INGOT);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName((int)Math.pow(10, i) + "円");
                    itemMeta.setCustomModelData(i);
                    itemMeta.setLore(Arrays.asList("交易や電子マネーに変換することができる通貨"));
                    itemStack.setItemMeta(itemMeta);
                }
                if(i != 8){
                    inventory.setItem(i, itemStack);
                }else{
                    inventory.setItem(i-1, itemStack);
                }
            }

            player.openInventory(inventory);
            return true;
        }

        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equalsIgnoreCase("ATM")){
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem == null)return;
            for (String lore : clickedItem.getItemMeta().getLore()) {
                if (lore.equalsIgnoreCase("交易や電子マネーに変換することができる通貨")) {
                    int ClickedCustomModelData = clickedItem.getItemMeta().getCustomModelData();
                    player.sendMessage("クリック検出 " + ClickedCustomModelData);

                    //左クリックされた時の処理
                    if(event.isLeftClick()){
                        ItemStack[] contents = player.getInventory().getContents();
                        boolean found = false;
                        for(int i = 0; i < contents.length; i++){
                            ItemStack item = contents[i];
                            if(item != null && item.getType() == Material.GOLD_INGOT){
                                player.sendMessage("金インゴット検出");
                                ItemMeta meta = item.getItemMeta();
                                int MoneyCustomModelData = meta.getCustomModelData();

                                //クリックしたアイテムと同じ現金を持っていた時
                                if(MoneyCustomModelData == ClickedCustomModelData){
                                    player.sendMessage("同じアイテム検出");
                                    found = true;
                                    int amount = item.getAmount();
                                    if (amount == 1) {
                                        // インベントリからアイテムを削除
                                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        // アイテムの数量を1つ減らす
                                        item.setAmount(amount - 1);
                                        player.getInventory().setItem(i, item);
                                    }
                                    break;
                                }
                            }
                        }
                        if(!found) player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "現金を持っていません");
                    }
                    else if (event.isRightClick()) {
                        Bukkit.getServer().broadcastMessage(ClickedCustomModelData + " is Right Clicked");
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}

