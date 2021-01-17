package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import me.zachary.duel.utils.LocationUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {
    private List<Arena> arenas = new ArrayList<>();
    private Duel plugin;

    public ArenaManager(Duel plugin) {
        this.plugin = plugin;
    }

    public void clearArena(){
        this.arenas.clear();
    }

    public void loadArena(){
        try {
            for(String string : plugin.getConfigurationSection().getKeys(false)) {
                String loc1 = (String) plugin.getConfigurationSection().get(string + ".loc1");
                String loc2 = (String) plugin.getConfigurationSection().get(string + ".loc2");
                String world = (String) plugin.getConfigurationSection().get(string + ".world");
                Arena arena = new Arena(string, LocationUtils.parseStringToLoc(loc1, world), LocationUtils.parseStringToLoc(loc2, world));
                this.addArena(arena);
            }
        }
        catch(Exception e) {
            System.out.println("You don't have create arena yet!");
        }
    }

    public void joinArena(Player firstPlayer, Player secondPlayer){
        Arena nextArena = getNextArena();
        if(nextArena != null){
            nextArena.getPlayers().add(firstPlayer);
            nextArena.getPlayers().add(secondPlayer);
            firstPlayer.teleport(nextArena.getFirstLoc());
            secondPlayer.teleport(nextArena.getSecondLoc());
            nextArena.setStarted();
        }
    }

    public void addArena(Arena arena) {
        this.arenas.add(arena);
    }

    public List<Arena> getArenas(){
        return arenas;
    }

    public Arena getArenaByPlayer(Player player) {
        for (Arena arena : arenas)
            if (arena.getPlayers().contains(player))
                return arena;
        return null;
    }

    private Arena getNextArena() {
        for (Arena arena : arenas)
            if (!arena.isStarted())
                return arena;
        return null;
    }
}
