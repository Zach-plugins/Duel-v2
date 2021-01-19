package me.zachary.duel;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {
    private Duel plugin;

    public Placeholder(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "duel";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Zach_FR";
    }

    @Override
    public String getRequiredPlugin(){
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // %duel_lose%
        if(identifier.equals("lose")){
            return String.valueOf(plugin.getDatabaseManager().getPlayerLose().get(player));
        }

        // %duel_win%
        if(identifier.equals("win")){
            return String.valueOf(plugin.getDatabaseManager().getPlayerWin().get(player));
        }

        return null;
    }
}
