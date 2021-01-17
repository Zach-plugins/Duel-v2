package me.zachary.duel;

import me.zachary.duel.arenas.ArenaListeners;
import me.zachary.duel.arenas.ArenaManager;
import me.zachary.duel.commands.CommandManager;
import me.zachary.zachcore.ZachCorePlugin;
import me.zachary.zachcore.guis.ZachGUI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Duel extends ZachCorePlugin {
    public static ZachGUI zachGUI;
    // Ideally Player who need accept duel :: Player who request the duel please.
    // Basically, requested :: requester
    public Map<Player, Player> players = new HashMap<>();
    public ArenaManager arenaManager = new ArenaManager(this);
    private File arenaFile;
    public YamlConfiguration arenaConfig;

    @Override
    public void onEnable() {
        zachGUI = new ZachGUI(this);
        loadArenaConfig();
        getArenaManager().loadArena();
        getServer().getPluginManager().registerEvents(new ArenaListeners(this), this);

        new CommandManager(this);

        preEnable();
    }

    @Override
    public void onDisable() {

    }

    public void loadArenaConfig() {
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        arenaFile = new File(getDataFolder() + File.separator + "arenas.yml");

        if (!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
    }

    public void saveArenaConfig() {
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ZachGUI getGUI() {
        return zachGUI;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public ConfigurationSection getConfigurationSection(){
        return arenaConfig.getConfigurationSection("arenas");
    }
}
