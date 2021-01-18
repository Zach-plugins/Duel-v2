package me.zachary.duel.arenas;

import me.zachary.duel.Duel;
import me.zachary.duel.kits.Kit;
import me.zachary.duel.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArenaManager {
    static Map<UUID, ItemStack[]> items = new HashMap<UUID, ItemStack[]>();
    static Map<UUID, ItemStack[]> armor = new HashMap<UUID, ItemStack[]>();
    static Map<UUID, Location> locations = new HashMap<UUID, Location>();

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
            for(String string : plugin.getConfigurationSectionArena().getKeys(false)) {
                String loc1 = (String) plugin.getConfigurationSectionArena().get(string + ".loc1");
                String loc2 = (String) plugin.getConfigurationSectionArena().get(string + ".loc2");
                String world = (String) plugin.getConfigurationSectionArena().get(string + ".world");
                String material = (String) plugin.getConfigurationSectionArena().get(string + ".material");
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

    public void joinArena(Player player, Arena arena, Kit kit){
        if(arena != null){
            arena.getPlayers().add(player);
            arena.getPlayersKit().put(player, kit);
            if(arena.getPlayers().size() == 2){
                initializeDuel(arena.getPlayers().get(0), arena.getPlayers().get(1), arena);
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
        for(String string : plugin.getConfigurationSectionArena().getKeys(false)) {
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

    public void initializeDuel(Player firstPlayer, Player secondPlayer, Arena arena){
        saveLocations(firstPlayer);
        saveLocations(secondPlayer);
        firstPlayer.teleport(arena.getFirstLoc());
        secondPlayer.teleport(arena.getSecondLoc());
        firstPlayer.setGameMode(GameMode.SURVIVAL);
        secondPlayer.setGameMode(GameMode.SURVIVAL);
        firstPlayer.setHealth(20);
        secondPlayer.setHealth(20);
        firstPlayer.setFoodLevel(20);
        secondPlayer.setFoodLevel(20);
        storeAndClearInventory(firstPlayer);
        storeAndClearInventory(secondPlayer);
        setStuff(firstPlayer, arena.getPlayersKit().get(firstPlayer));
        setStuff(secondPlayer, arena.getPlayersKit().get(secondPlayer));
    }

    public static void saveLocations(Player player) {
        locations.put(player.getUniqueId(), player.getLocation());
    }

    public static void restoreLocations(Player player) {
        Location loc = locations.get(player.getUniqueId());
        player.teleport(loc);
    }

    public static void storeAndClearInventory(Player player){
        UUID uuid = player.getUniqueId();

        ItemStack[] cont = player.getInventory().getContents();
        ItemStack[] armcont = player.getInventory().getArmorContents();

        items.put(uuid, cont);
        armor.put(uuid, armcont);

        player.getInventory().clear();

        remArmor(player);
    }

    public static void remArmor(Player player){
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void restoreInventory(Player player){
        UUID uuid = player.getUniqueId();

        ItemStack[] contents = items.get(uuid);
        ItemStack[] armorContents = armor.get(uuid);

        if(contents != null){
            player.getInventory().setContents(contents);
            items.remove(uuid);
        }
        else{ //if the player has no inventory contents, clear their inventory
            player.getInventory().clear();
        }

        if(armorContents != null){
            player.getInventory().setArmorContents(armorContents);
            armor.remove(uuid);
        }
        else{ //if the player has no armor, set the armor to null
            remArmor(player);
        }
    }

    public void setStuff(Player player, Kit kit){
        player.getInventory().setHelmet(kit.getHelmet());
        player.getInventory().setChestplate(kit.getChestplate());
        player.getInventory().setLeggings(kit.getLeggings());
        player.getInventory().setBoots(kit.getBoots());
        for (ItemStack itemStack : kit.getKit().getContents()) {
            if(itemStack != null)
                player.getInventory().addItem(itemStack);
        }
    }
}
