package com.edamame.edamamebank.database;

import org.bukkit.Bukkit;

import java.sql.*;

public class Database {
    private final String BD_name = "database.db";
    private Connection connection;
    private Statement statement;

    public void CreateConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.BD_name);
            this.statement = connection.createStatement();

            this.statement.executeUpdate("create table moneydata(uuid text, money integer");
            Bukkit.getLogger().info("ーーーーEdamameBankーーーー");
            Bukkit.getLogger().info("database.dbを作成しました");
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
}
