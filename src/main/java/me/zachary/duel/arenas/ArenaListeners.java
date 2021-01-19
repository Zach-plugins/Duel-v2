package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import me.zachary.zachcore.utils.ChatUtils;
import me.zachary.zachcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
            event.getDrops().clear();
            event.setKeepLevel(true);
            Bukkit.broadcastMessage(ChatUtils.color(plugin.getMessageManager().getString("Duel broadcast win").replace("{winner}", killer.getName()).replace("{loser}", victim.getName())));

            Player finalKiller = killer;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getArenaManager().restoreInventory(finalKiller);
                    finalKiller.setHealth(20);
                    plugin.getArenaManager().restoreLocations(finalKiller);
                }
            }, 100L);
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event){
        Player victim = event.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(victim);
        if(arena != null && arena.isStarted()){
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getArenaManager().restoreInventory(victim);
                    plugin.getArenaManager().restoreLocations(victim);
                }
            }, 5L);
            arena.eliminate(victim);
        }
    }


    // if player have doesn't have "duel.bypass.command" permission it's should block the command.
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
        if(arena != null && !player.hasPermission("duel.bypass.command")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player leaver = event.getPlayer();
        Player otherPlayer = null;
        Arena arena = plugin.getArenaManager().getArenaByPlayer(leaver);
        if(arena != null) {
            if(leaver == arena.getPlayers().get(0)){
                otherPlayer = arena.getPlayers().get(1);
            }else{
                otherPlayer = arena.getPlayers().get(0);
            }
            plugin.getArenaManager().restoreLocations(leaver);
            plugin.getArenaManager().restoreLocations(otherPlayer);
            plugin.getArenaManager().restoreInventory(leaver);
            plugin.getArenaManager().restoreInventory(otherPlayer);
            MessageUtils.sendMessage(otherPlayer, plugin.getMessageManager().getString("Duel leave").replace("{leaver}", leaver.getName()));
            arena.eliminate(leaver);
        }

    }
}
