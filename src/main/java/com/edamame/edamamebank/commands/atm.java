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
                    itemMeta.setLore(Arrays.asList("左クリックで現金を電子マネーに入金する","シフトと左クリックで現金を64個入金する", "右クリックで電子マネーから現金を出金する", "シフトと右クリックで現金を64個出金する"));
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

                    //左クリックされた時の処理
                    if(event.isLeftClick()){
                        ItemStack[] contents = player.getInventory().getContents();
                        boolean found = false;
                        for(int i = 0; i < contents.length; i++){
                            ItemStack item = contents[i];
                            if(item != null && item.getType() == Material.GOLD_INGOT){
                                ItemMeta meta = item.getItemMeta();
                                int MoneyCustomModelData = meta.getCustomModelData();

                                //クリックしたアイテムと同じ現金を持っていた時
                                if(MoneyCustomModelData == ClickedCustomModelData){
                                    int amount = item.getAmount();
                                    //シフトクリックしたとき
                                    if(event.isShiftClick()){
                                        if (amount == 64) {
                                            // インベントリからアイテムを削除
                                            player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                            found = true;
                                            database.AddMoney(player, 64 * (int)Math.pow(10, MoneyCustomModelData));
                                            break;
                                        }
                                    }else {
                                        if (amount == 1) {
                                            // インベントリからアイテムを削除
                                            player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                        } else {
                                            // アイテムの数量を1つ減らす
                                            item.setAmount(amount - 1);
                                            player.getInventory().setItem(i, item);
                                        }
                                        found = true;
                                        database.AddMoney(player, (int)Math.pow(10, MoneyCustomModelData));
                                        break;
                                    }
                                }
                            }
                        }
                        if(!found) player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "現金を持っていません");
                    }
                    //右クリックされた時の処理
                    else if (event.isRightClick()) {
                        ItemStack giveMoney = new ItemStack(Material.GOLD_INGOT);
                        ItemMeta giveMoneyMeta = giveMoney.getItemMeta();
                        giveMoneyMeta.setDisplayName((int)Math.pow(10, ClickedCustomModelData) + "円");
                        giveMoneyMeta.setCustomModelData(ClickedCustomModelData);
                        giveMoneyMeta.setLore(Arrays.asList("交易や電子マネーに変換することができる通貨"));
                        giveMoney.setItemMeta(giveMoneyMeta);

                        int haveMoney = database.CheckMoney(player);
                        if(event.isShiftClick()){
                            int needMoney =  64 * (int) Math.pow(10, ClickedCustomModelData);
                            if(haveMoney >= needMoney){
                                boolean gave = false;

                                haveMoney = haveMoney - needMoney;
                                ItemStack[] contents = player.getInventory().getContents();

                                for(int i = 0; i < contents.length; i++){
                                    ItemStack item = contents[i];

                                    if(item == null) {
                                        giveMoney.setAmount(64);
                                        player.getInventory().setItem(i, giveMoney);
                                        database.AddMoney(player, -64 * (int)Math.pow(10, ClickedCustomModelData));
                                        gave = true;
                                        break;
                                    }
                                }
                                if(!gave)player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "インベントリに空きがありません");
                            }else{
                                player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "電子マネーがが足りません");
                            }
                        }
                        else{
                            int needMoney =  (int) Math.pow(10, ClickedCustomModelData);
                            if(haveMoney >= needMoney){
                                boolean gave = false;

                                haveMoney = haveMoney - needMoney;
                                ItemStack[] contents = player.getInventory().getContents();

                                for(int i = 0; i < contents.length; i++){
                                    ItemStack item = contents[i];

                                    if(item != null && item.getType() == Material.GOLD_INGOT && item.getAmount() != 64 && item.getItemMeta().getCustomModelData() == ClickedCustomModelData) {
                                        //アイテムの数を1つ増やす
                                        item.setAmount(item.getAmount() + 1);
                                        player.getInventory().setItem(i, item);
                                        database.AddMoney(player, -1 * (int)Math.pow(10, ClickedCustomModelData));
                                        gave = true;
                                        break;
                                    }
                                }
                                if(!gave){
                                    for(int i = 0; i < contents.length; i++){
                                        ItemStack item = contents[i];

                                        if(item == null) {
                                            player.getInventory().setItem(i, giveMoney);
                                            database.AddMoney(player, -1 * (int)Math.pow(10, ClickedCustomModelData));
                                            gave = true;
                                            break;
                                        }
                                    }
                                    if(!gave)player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "インベントリに空きがありません");
                                }
                            }else{
                                player.sendMessage(ChatColor.RED + "[edamameBank] " + ChatColor.WHITE + "電子マネーがが足りません");
                            }
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}

