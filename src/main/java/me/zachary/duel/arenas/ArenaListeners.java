package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaListeners implements Listener {
    private Duel plugin;

    public ArenaListeners(Duel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        Player victim = event.getEntity();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(event.getEntity());
        Player killer = null;
        if(arena != null){
            if(victim == arena.getPlayers().get(0)){
                killer = arena.getPlayers().get(1);
            }else{
                killer = arena.getPlayers().get(0);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event){
        System.out.println(event);
        Player victim = event.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(victim);
        if(arena != null && arena.isStarted()){
            System.out.println(arena.getArenaName());
            arena.eliminate(victim);
        }
    }
}
