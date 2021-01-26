package me.zachary.duel.database;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;
import me.zachary.duel.Duel;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseManager {
    private Duel plugin;
    private Database sql;
    private Map<Player, Integer> playerWin = new HashMap<Player, Integer>();
    private Map<Player, Integer> playerLose = new HashMap<Player, Integer>();

    public DatabaseManager(Duel plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase(){
        if(plugin.getConfig().getBoolean("MySQL.Enabled")){
            sql = new MySQL(Logger.getLogger("Minecraft"),
                    "[Duel] ",
                    plugin.getConfig().getString("MySQL.Hostname"),
                    plugin.getConfig().getInt("MySQL.Port"),
                    plugin.getConfig().getString("MySQL.Database"),
                    plugin.getConfig().getString("MySQL.Username"),
                    plugin.getConfig().getString("MySQL.Password"));
        }else{
            sql = new SQLite(Logger.getLogger("Minecraft"),
                    "[Duel] ",
                    plugin.getDataFolder().getAbsolutePath(),
                    "Duel",
                    ".sqlite");
        }

        if (!sql.open()) {
            sql.open();
        }
        if(sql.open()){
            try {
                if(!sql.isTable("Duel_Lose"))
                    sql.query("CREATE TABLE `Duel_Lose` (`uuid` TEXT, `lose` INT DEFAULT '0');");
                if(!sql.isTable("Duel_Win"))
                    sql.query("CREATE TABLE `Duel_Win` (`uuid` TEXT, `win` INT DEFAULT '0');");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Map<Player, Integer> getPlayerWin(){
        return playerWin;
    }

    public Map<Player, Integer> getPlayerLose(){
        return playerLose;
    }

    public int getWin(Player player){
        if (!sql.open()) {
            sql.open();
        }
        Boolean bool = null;
        int win = 0;
        try {
            ResultSet result = sql.query("SELECT EXISTS(SELECT * FROM Duel_Win WHERE uuid = '"+ player.getUniqueId() +"');");
            result.next();
            bool = result.getBoolean(1);
            if(bool){
                ResultSet resultTime =  sql.query("SELECT win FROM Duel_Win WHERE uuid = '"+ player.getUniqueId() +"';");
                resultTime.next();
                win = resultTime.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return win;
    }

    public void setWin(Player player, int winNumberToAdd){
        if (!sql.open()) {
            sql.open();
        }
        Boolean bool = null;
        int win = 0;
        try {
            ResultSet result = sql.query("SELECT EXISTS(SELECT * FROM Duel_Win WHERE uuid = '"+ player.getUniqueId() +"');");
            result.next();
            bool = result.getBoolean(1);
            if(bool){
                ResultSet resultSet = sql.query("SELECT win FROM Duel_Win WHERE uuid = '"+ player.getUniqueId() +"';");
                resultSet.next();
                win = resultSet.getInt(1);
                sql.query("UPDATE Duel_Win SET uuid = '"+ player.getUniqueId() +"', win = '"+ (win + winNumberToAdd) +"' WHERE uuid = '"+ player.getUniqueId() +"';");
            }else{
                sql.query("INSERT INTO Duel_Win (uuid,win) VALUES ('"+ player.getUniqueId() +"',"+ (win + winNumberToAdd) +");");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getPlayerWin().put(player, (win + winNumberToAdd));
    }

    public int getLose(Player player){
        if (!sql.open()) {
            sql.open();
        }
        Boolean bool = null;
        int lose = 0;
        try {
            ResultSet result = sql.query("SELECT EXISTS(SELECT * FROM Duel_Lose WHERE uuid = '"+ player.getUniqueId() +"');");
            result.next();
            bool = result.getBoolean(1);
            if(bool){
                ResultSet resultTime =  sql.query("SELECT lose FROM Duel_Lose WHERE uuid = '"+ player.getUniqueId() +"';");
                resultTime.next();
                lose = resultTime.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lose;
    }

    public void setLose(Player player, int loseNumberToAdd){
        if (!sql.open()) {
            sql.open();
        }
        Boolean bool = null;
        int lose = 0;
        try {
            ResultSet result = sql.query("SELECT EXISTS(SELECT * FROM Duel_Lose WHERE uuid = '"+ player.getUniqueId() +"');");
            result.next();
            bool = result.getBoolean(1);
            if(bool){
                ResultSet resultSet = sql.query("SELECT lose FROM Duel_Lose WHERE uuid = '"+ player.getUniqueId() +"';");
                resultSet.next();
                lose = resultSet.getInt(1);
                sql.query("UPDATE Duel_Lose SET uuid = '"+ player.getUniqueId() +"', lose = '"+ (lose + loseNumberToAdd) +"' WHERE uuid = '"+ player.getUniqueId() +"';");
            }else{
                sql.query("INSERT INTO Duel_Lose (uuid,lose) VALUES ('"+ player.getUniqueId() +"',"+ (lose + loseNumberToAdd) +");");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getPlayerLose().put(player, (lose + loseNumberToAdd));
    }

    public void closeConnection(){
        sql.close();
    }
}
