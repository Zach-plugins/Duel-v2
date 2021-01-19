package me.zachary.duel.listeners;

import me.zachary.duel.Duel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private Duel plugin;

    public JoinListener(Duel plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.getDatabaseManager().getPlayerLose().put(player, plugin.getDatabaseManager().getLose(player));
        plugin.getDatabaseManager().getPlayerWin().put(player, plugin.getDatabaseManager().getWin(player));
    }
}
