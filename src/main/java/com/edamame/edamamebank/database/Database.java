package com.edamame.edamamebank.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

            Bukkit.getLogger().info("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().info("データベースに接続しました");
            Bukkit.getLogger().info("ーーーーーーーーーーーーーーー");
        }
        catch (Exception e){
            Bukkit.getLogger().warning("ーーーーedamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void CreateTable(){
        try{
            this.statement.executeUpdate("create table moneydata(uuid text,name text, money integer)");
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

    public void AddPlayerData(Player player, Player sender){
        try {
            String uuid = player.getUniqueId().toString();
            String name = player.getDisplayName();

            this.statement.executeUpdate("insert into moneydata values('" + uuid + "', '" + name + "', 0)");
            sender.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + name +
                    ChatColor.WHITE + ChatColor.BOLD +  "の口座が作成されました");
        }
        catch (SQLException e) {
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void AddMoney(Player player, int add_money, Player sender){
        try{
            String uuid = player.getUniqueId().toString();

            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");
            int money = resultSet.getInt("money");

            money = money + add_money;

            this.statement.executeUpdate("update moneydata set money = " + money + " where uuid = '" + uuid + "'");

            sender.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + player.getDisplayName() +
                    ChatColor.WHITE + ChatColor.BOLD + "のお金を" +
                    ChatColor.YELLOW + ChatColor.BOLD +add_money +
                    ChatColor.WHITE + ChatColor.BOLD + "円追加しました");

            //sender.sendMessage("[edamameBank] " + player.getDisplayName() + "の現在の所持金は" + money + "円です");
            resultSet.close();
        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void CheckMoney(Player player){
        String uuid = player.getUniqueId().toString();
        String name = player.getDisplayName();

        try {
            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");
            int money = resultSet.getInt("money");

            player.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + name +
                    ChatColor.WHITE + ChatColor.BOLD + "の現在の所持金は" +
                    ChatColor.YELLOW + ChatColor.BOLD + money +
                    ChatColor.WHITE + ChatColor.BOLD + "円です");

            resultSet.close();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void PayMoney(Player sender, Player receiver, int money){
        String sender_uuid = sender.getUniqueId().toString();
        String sender_name = sender.getDisplayName();
        String receiver_uuid = receiver.getUniqueId().toString();
        String receiver_name = receiver.getDisplayName();

        try {
            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + sender_uuid + "'");
            int sender_money = resultSet.getInt("money");
            resultSet = statement.executeQuery("select money from moneydata where uuid = '" + receiver_uuid + "'");
            int receiver_money = resultSet.getInt("money");

            sender_money = sender_money - money;
            receiver_money = receiver_money + money;

            resultSet.close();

            this.statement.executeUpdate("update moneydata set money = " + sender_money + " where uuid = '" + sender_uuid + "'");
            this.statement.executeUpdate("update moneydata set money = " + receiver_money + " where uuid = '" + receiver_uuid + "'");

            sender.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + receiver_name +
                    ChatColor.WHITE + ChatColor.BOLD + "に" +
                    ChatColor.YELLOW + ChatColor.BOLD + money +
                    ChatColor.WHITE + ChatColor.BOLD + "円送金しました");

            receiver.sendMessage(ChatColor.GREEN + "[edamameBank] " +
                    ChatColor.YELLOW + ChatColor.BOLD + sender_name +
                    ChatColor.WHITE + ChatColor.BOLD + "から" +
                    ChatColor.YELLOW + ChatColor.BOLD + money +
                    ChatColor.WHITE + ChatColor.BOLD + "円を受け取りました");


        } catch (SQLException e) {
            Bukkit.getLogger().warning("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }
}
