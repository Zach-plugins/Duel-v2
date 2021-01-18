package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import me.zachary.duel.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
                String material = (String) plugin.getConfigurationSection().get(string + ".material");
                Arena arena = new Arena(string, Material.valueOf(material), LocationUtils.parseStringToLoc(loc1, world), LocationUtils.parseStringToLoc(loc2, world));
                this.addArena(arena);
            }
        }
        catch(Exception ex) {
            System.out.println("You don't have create arena yet!");
        }
    }

    public void joinArena(Player firstPlayer, Player secondPlayer){
        this.joinArena(firstPlayer, secondPlayer, null);
    }

    public void joinArena(Player player, Arena arena){
        if(arena != null){
            arena.getPlayers().add(player);
            if(arena.getPlayers().size() == 2){
                arena.getPlayers().get(0).teleport(arena.getFirstLoc());
                arena.getPlayers().get(1).teleport(arena.getSecondLoc());
                arena.setStarted();
            }
        }
    }

    public void joinArena(Player firstPlayer, Player secondPlayer, Arena arena){
        if(arena != null){
            arena.getPlayers().add(firstPlayer);
            arena.getPlayers().add(secondPlayer);
            firstPlayer.teleport(arena.getFirstLoc());
            secondPlayer.teleport(arena.getSecondLoc());
            arena.setStarted();
        }
    }

    public void addArena(Arena arena) {
        this.arenas.add(arena);
    }

    public void reloadArena(){
        clearArena();
        loadArena();
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

    public Arena getArenaByName(String arenaName) {
        for (Arena arena : arenas)
            if (arena.getArenaName().equals(arenaName))
                return arena;
        return null;
    }

    public void deleteArena(Arena arena) {
        for(String string : plugin.getConfigurationSection().getKeys(false)) {
            if(arena.getArenaName().equals(string)){
                plugin.arenaConfig.set("arenas." + string, null);
                plugin.saveArenaConfig();
                clearArena();
                loadArena();
            }
        }
    }

    private Arena getNextArena() {
        for (Arena arena : arenas)
            if (!arena.isStarted())
                return arena;
        return null;
    }
}
