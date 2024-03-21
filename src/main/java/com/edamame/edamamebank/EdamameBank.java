package com.edamame.edamamebank;

import com.edamame.edamamebank.commands.*;
import com.edamame.edamamebank.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;

public final class EdamameBank extends JavaPlugin {
    Database database = new Database();

    @Override
    public void onEnable() {
        // コマンド実行処理のクラス分け
        getCommand("bankcreate").setExecutor(new createbank());
        getCommand("addmoney").setExecutor(new addmoney());
        getCommand("bank").setExecutor(new checkbank());
        getCommand("pay").setExecutor(new paymoney());
        getCommand("atm").setExecutor(new atm());
        Bukkit.getPluginManager().registerEvents(new atm(), this);

        database.CreateTable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        database.CloseConnection();
    }
}
