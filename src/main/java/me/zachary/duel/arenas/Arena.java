package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private Location loc1;
    private Location loc2;
    private List<Player> players;
    private String arenaName;
    private boolean isStarted;
    private Duel plugin;

    public Arena(Duel plugin) {
        this.plugin = plugin;
    }

    public Arena(String arenaName, Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.arenaName = arenaName;
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

    public String getArenaName(){
        return arenaName;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void eliminate(Player victim) {
        players.remove(victim);
        checkWin();
    }

    private void checkWin() {

        if (players.size() == 1) {
            Player winner = players.get(0);
            restart();
        }

    }

    private void restart() {
        this.players = new ArrayList<>();
        this.isStarted = false;
    }

    public void setStarted() {
        this.isStarted = true;
    }
}
