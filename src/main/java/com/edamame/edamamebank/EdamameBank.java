package com.edamame.edamamebank;

import com.edamame.edamamebank.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;

public final class EdamameBank extends JavaPlugin {
    Database database = new Database();

    @Override
    public void onEnable() {
        // Plugin startup logic
        database.CreateConnection();
        database.CreateTable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        database.CloseConnection();
    }
}
