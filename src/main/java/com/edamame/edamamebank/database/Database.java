package com.edamame.edamamebank.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database {
    private final String BD_name = "database.db";
    private Connection connection = null;
    private Statement statement = null;

    public Database() {
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.BD_name);
            this.statement = connection.createStatement();
        }
        catch (Exception e){
            Bukkit.getLogger().warning("ーーーーedamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void CreateTable(){
        try{
            this.statement.executeUpdate("create table moneydata(uuid text, money integer)");
            Bukkit.getLogger().info("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().info("moneydataテーブルを作成しました");
            Bukkit.getLogger().info("ーーーーーーーーーーーーーーー");
            //this.connection.commit();
        }
        catch(SQLException e){
                Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
                Bukkit.getLogger().warning(e.toString());
                Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
            }
    }

    public void CloseConnection(){
        try{this.connection.close();}
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void AddPlayerData(String uuid, Player player){
        try {
            String name = player.getDisplayName();

            this.statement.executeUpdate("insert into moneydata values('" + uuid + "', 0)");
            player.sendMessage("[edamameBank] " + name + "の口座が作成されました");
        }
        catch (SQLException e) {
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void AddMoney(Player player, int add_money){
        try{
            String uuid = player.getUniqueId().toString();

            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");
            int money = resultSet.getInt("money");

            money = money + add_money;

            this.statement.executeUpdate("update moneydata set money = " + money + " where uuid = '" + uuid + "'");

            player.sendMessage("[edamameBank] " + player.getDisplayName() + "のお金を" + add_money + "円追加しました");
            player.sendMessage("[edamameBank] " + player.getDisplayName() + "の現在の所持金は" + money + "円です");

        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }
}
