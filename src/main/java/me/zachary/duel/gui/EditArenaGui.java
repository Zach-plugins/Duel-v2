package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.utils.LocationUtils;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.ChatPromptUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EditArenaGui {
    private Duel plugin;

    public EditArenaGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getEditArenaGui(){
        ZMenu editArenaGui = Duel.zachGUI.create("&6&lEdit arena menu", 6);
        editArenaGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for (Arena arena : plugin.getArenaManager().getArenas()) {
            ZButton arenaButton = new ZButton(new ItemBuilder(XMaterial.valueOf(arena.getMaterial().name()).parseMaterial())
            .name(arena.getArenaName())
            .lore(
                    "&7Location 1: &8" + LocationUtils.unparseLocToString(arena.getFirstLoc()),
                    "&7Location 2: &8" + LocationUtils.unparseLocToString(arena.getSecondLoc()),
                    "&7World name: &8" + arena.getWorldName(),
                    "&7Available: &8" + !arena.isStarted()
            ).build()).withListener(inventoryClickEvent -> {
                inventoryClickEvent.getWhoClicked().openInventory(getAdvancedEditArena((Player) inventoryClickEvent.getWhoClicked(), plugin.getArenaManager().getArenaByName(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName())));
            });
            editArenaGui.setButton(slot, arenaButton);
            slot++;
        }

        return editArenaGui.getInventory();
    }

    public Inventory getAdvancedEditArena(Player player, Arena arena){
        ZMenu advancedEditArena = Duel.getGUI().create("&6Edit arena: " + arena.getArenaName(), 1);
        advancedEditArena.setAutomaticPaginationEnabled(false);

        ZButton renameButton = new ZButton(new ItemBuilder(XMaterial.valueOf("ANVIL").parseMaterial())
                .name("&aRename the arena")
                .lore(
                        "&7Click here to",
                        "&7rename arena.",
                        "&8Current name: &7" + arena.getArenaName()
                ).build()).withListener(inventoryClickEvent -> {
            ChatPromptUtils.showPrompt(plugin, player, "&6Enter new name of arena.", chatConfirmEvent -> {
                String[] info = new String[4];
                info[0] = plugin.getConfigurationSection().getString(arena.getArenaName() + ".loc1");
                info[1] = plugin.getConfigurationSection().getString(arena.getArenaName() + ".loc2");
                info[2] = plugin.getConfigurationSection().getString(arena.getArenaName() + ".material");
                info[3] = plugin.getConfigurationSection().getString(arena.getArenaName() + ".world");
                plugin.getConfigurationSection().set(chatConfirmEvent.getMessage() + ".loc1", info[0]);
                plugin.getConfigurationSection().set(chatConfirmEvent.getMessage() + ".loc2", info[1]);
                plugin.getConfigurationSection().set(chatConfirmEvent.getMessage() + ".material", info[2]);
                plugin.getConfigurationSection().set(chatConfirmEvent.getMessage() + ".world", info[3]);
                plugin.saveArenaConfig();
                plugin.getArenaManager().deleteArena(arena);
            });
        });
        ZButton loc1Button = new ZButton(new ItemBuilder(XMaterial.valueOf("COMPASS").parseMaterial())
                .name("&aChange location 1")
                .lore(
                        "&7Click here to",
                        "&7change location 1.",
                        "&8Current name: &7" + LocationUtils.unparseLocToString(arena.getFirstLoc())
                ).build()).withListener(inventoryClickEvent -> {
            ChatPromptUtils.showPrompt(plugin, player, "&6Enter new location 1. Please use this format: &lX,Y,Z", chatConfirmEvent -> {
                plugin.getConfigurationSection().set(arena.getArenaName() + ".loc1", chatConfirmEvent.getMessage());
                plugin.saveArenaConfig();
                plugin.getArenaManager().reloadArena();
            });
        });
        ZButton loc2Button = new ZButton(new ItemBuilder(XMaterial.valueOf("COMPASS").parseMaterial())
                .name("&aChange location 2")
                .lore(
                        "&7Click here to",
                        "&7change location 2.",
                        "&8Current name: &7" + LocationUtils.unparseLocToString(arena.getSecondLoc())
                ).build()).withListener(inventoryClickEvent -> {
            ChatPromptUtils.showPrompt(plugin, player, "&6Enter new location 2. Please use this format: &lX,Y,Z", chatConfirmEvent -> {
                plugin.getConfigurationSection().set(arena.getArenaName() + ".loc2", chatConfirmEvent.getMessage());
                plugin.saveArenaConfig();
                plugin.getArenaManager().reloadArena();
            });
        });
        ZButton worldButton = new ZButton(new ItemBuilder(XMaterial.valueOf("GRASS_BLOCK").parseMaterial())
                .name("&aChange world")
                .lore(
                        "&7Click here to",
                        "&7change world name.",
                        "&8Current world: &7" + arena.getWorldName()
                ).build()).withListener(inventoryClickEvent -> {
            String worldAvailable = "";
            int index = Bukkit.getWorlds().size();
            for (World world : Bukkit.getWorlds()) {
                if(index != 1)
                    worldAvailable += "&e" + world.getName() + "&6,";
                else
                    worldAvailable += "&e" + world.getName();
                index--;
            }
            ChatPromptUtils.showPrompt(plugin, player, "&6Enter new world name. World available: \n" + worldAvailable + "&6.", chatConfirmEvent -> {
                plugin.getConfigurationSection().set(arena.getArenaName() + ".world", chatConfirmEvent.getMessage());
                plugin.saveArenaConfig();
                plugin.getArenaManager().reloadArena();
            });
        });
        ZButton materialButton = new ZButton(new ItemBuilder(XMaterial.valueOf(arena.getMaterial().name()).parseMaterial())
                .name("&aChange arena icon")
                .lore(
                        "&7Click here to",
                        "&7change arena icon.",
                        "&8Current material: &7" + arena.getMaterial().name()
                ).build()).withListener(inventoryClickEvent -> {
            ChatPromptUtils.showPrompt(plugin, player, "&6Enter new material name.", chatConfirmEvent -> {
                plugin.getConfigurationSection().set(arena.getArenaName() + ".material", chatConfirmEvent.getMessage().toUpperCase());
                plugin.saveArenaConfig();
                plugin.getArenaManager().reloadArena();
            });
        });
        ZButton deleteButton = new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE_BLOCK").parseMaterial())
                .name("&cDelete arena")
                .lore(
                        "&7Click here to",
                        "&7delete arena."
                ).build()).withListener(inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().openInventory(new ConfirmGui(plugin).getConfirmGui((Player) inventoryClickEvent.getWhoClicked(), "&6Confirmation", "&cThis will permanently delete &c&lTest&c.", "&7Click here to cancel arena deletion.", () -> {
                plugin.getArenaManager().deleteArena(arena);
            }));
        });

        advancedEditArena.setButton(0, renameButton);
        advancedEditArena.setButton(1, loc1Button);
        advancedEditArena.setButton(2, loc2Button);
        advancedEditArena.setButton(3, worldButton);
        advancedEditArena.setButton(4, materialButton);
        advancedEditArena.setButton(8, deleteButton);

        return advancedEditArena.getInventory();
    }
}
