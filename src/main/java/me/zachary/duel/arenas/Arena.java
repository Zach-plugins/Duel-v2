package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import me.zachary.duel.kits.Kit;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.hooks.EconomyManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {
    private Location loc1;
    private Location loc2;
    private List<Player> players;
    private Map<Player, Kit> playerKit;
    private String arenaName;
    private String worldName;
    private Material material;
    private boolean isStarted;
    private Duel plugin;

    public Arena(String arenaName, Material material, Location loc1, Location loc2) {
        this.plugin = JavaPlugin.getPlugin(Duel.class);
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.arenaName = arenaName;
        this.material = material;
        restart();
    }

    public Location getFirstLoc() {
        return loc1;
    }

    public Location getSecondLoc() {
        return loc2;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Player, Kit> getPlayersKit(){
        return playerKit;
    }

    public String getArenaName(){
        return arenaName;
    }

    public String getWorldName(){
        return loc1.getWorld().getName();
    }

    public Material getMaterial(){
        return material;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void eliminate(Player victim) {
        players.remove(victim);
        checkWin(victim);
    }

    private void checkWin(Player victim) {

        if (players.size() == 1) {
            Player winner = players.get(0);
            double money = 0;
            if(plugin.bet1.containsKey(winner)){
                money += plugin.bet1.get(winner);
                plugin.bet1.remove(winner);
                money += plugin.bet2.get(victim);
                plugin.bet2.remove(victim);
            }else{
                money += plugin.bet1.get(victim);
                plugin.bet1.remove(victim);
                money += plugin.bet2.get(winner);
                plugin.bet2.remove(winner);
            }
            MessageUtils.sendMessage(winner, plugin.getMessageManager().getString("Duel win"));
            MessageUtils.sendMessage(victim, plugin.getMessageManager().getString("Duel lose"));
            plugin.getDatabaseManager().setWin(winner, 1);
            plugin.getDatabaseManager().setLose(victim, 1);
            EconomyManager.deposit(winner, money);
            restart();
        }

    }

    private void restart() {
        this.players = new ArrayList<>();
        this.playerKit = new HashMap<>();
        this.isStarted = false;
    }

    public void setStarted() {
        this.isStarted = true;
    }
}
