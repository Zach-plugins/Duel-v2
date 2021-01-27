package me.zachary.duel;

import me.zachary.duel.database.DatabaseManager;
import me.zachary.duel.arenas.ArenaListeners;
import me.zachary.duel.arenas.ArenaManager;
import me.zachary.duel.commands.CommandManager;
import me.zachary.duel.kits.KitManager;
import me.zachary.duel.listeners.JoinListener;
import me.zachary.updatechecker.Updatechecker;
import me.zachary.zachcore.ZachCorePlugin;
import me.zachary.zachcore.guis.ZachGUI;
import me.zachary.zachcore.utils.Metrics;
import me.zachary.zachcore.utils.hooks.EconomyManager;
import org.bukkit.Bukkit;
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
    public Map<Player, Double> bet1 = new HashMap<>();
    public Map<Player, Double> bet2 = new HashMap<>();
    public ArenaManager arenaManager = new ArenaManager(this);
    public KitManager kitManager = new KitManager(this);
    public DatabaseManager databaseManager = new DatabaseManager(this);
    public MessageManager messageManager = new MessageManager(this);
    private File arenaFile;
    private File kitFile;
    public YamlConfiguration arenaConfig;
    public YamlConfiguration kitConfig;

    @Override
    public void onEnable() {
        zachGUI = new ZachGUI(this);
        saveDefaultConfig();
        loadArenaConfig();
        loadKitConfig();
        getArenaManager().loadArena();
        getKitManager().loadKit();
        getServer().getPluginManager().registerEvents(new ArenaListeners(this), this);
        EconomyManager.load();
        new CommandManager(this);
        new JoinListener(this);
        getDatabaseManager().loadDatabase();
        getMessageManager().load();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new Placeholder(this).register();
        int pluginId = 9146;
        Metrics metrics = new Metrics(this, pluginId);
        Updatechecker.updateSongoda(this, 382);

        preEnable(this);
    }

    @Override
    public void onDisable() {
        getDatabaseManager().closeConnection();
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

    public void loadKitConfig() {
        kitFile = new File(getDataFolder() + File.separator + "kits.yml");

        if (!kitFile.exists()) {
            saveResource("kits.yml", false);
            kitFile = new File(getDataFolder() + File.separator + "kits.yml");
        }

        kitConfig = YamlConfiguration.loadConfiguration(kitFile);
    }

    public void saveArenaConfig() {
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKitconfig() {
        kitFile = new File(getDataFolder() + File.separator + "kits.yml");
        kitConfig = YamlConfiguration.loadConfiguration(kitFile);
    }

    public static ZachGUI getGUI() {
        return zachGUI;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public MessageManager getMessageManager(){
        return messageManager;
    }

    public ConfigurationSection getConfigurationSectionArena(){
        return arenaConfig.getConfigurationSection("arenas");
    }

    public ConfigurationSection getConfigurationSectionKit(){
        return kitConfig.getConfigurationSection("kits");
    }
}
