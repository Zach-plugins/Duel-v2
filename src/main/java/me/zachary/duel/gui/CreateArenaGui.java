package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.utils.LocationUtils;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateArenaGui {
    private Duel plugin;

    public CreateArenaGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getCreateArenaGui(Player player, Location loc1, Location loc2, String arenaName, String world) {
        ZMenu createArenaGui = Duel.getGUI().create("&6&lCreate arena", 1);
        createArenaGui.setAutomaticPaginationEnabled(false);
        ZButton renameButton = new ZButton(new ItemBuilder(XMaterial.valueOf("ANVIL").parseMaterial())
                .name("&aRename the arena!")
                .lore(
                        "&7Click here to",
                        "&7rename arena!",
                        "&8Current name: &7" + arenaName
                ).build()).withListener(inventoryClickEvent -> {
                    // Here
        });
        ZButton loc1Button = new ZButton(new ItemBuilder(XMaterial.valueOf("COMPASS").parseMaterial())
                .name("&aLocation 1")
                .lore(
                        "&7Click here to",
                        "&7change location 1",
                        "&8Current location 1: &7" + LocationUtils.unparseLocToString(loc1)
                ).build()).withListener(inventoryClickEvent -> {
                    // Here
        });
        ZButton loc2Button = new ZButton(new ItemBuilder(XMaterial.valueOf("COMPASS").parseMaterial())
                .name("&aLocation 2")
                .lore(
                        "&7Click here to",
                        "&7change location 2",
                        "&8Current location 2: &7" + LocationUtils.unparseLocToString(loc2)
                ).build()).withListener(inventoryClickEvent -> {
            /*do{
                ChatPromptUtils.showPrompt(plugin, player, "&6Please enter the new location of location 2. (Please use this format: \"X,Y,Z\")", chatConfirmEvent -> {
                    message.set(chatConfirmEvent.getMessage());
                    MessageUtils.sendMessage(player, "Test");
                });
                System.out.println(message);
            }while(LocationUtils.parseStringToLoc(message.get(), world) != null);*/
        });
        ZButton worldButton = new ZButton(new ItemBuilder(XMaterial.valueOf("GRASS_BLOCK").parseMaterial())
                .name("&aWorld name")
                .lore(
                        "&7Click here to",
                        "&7change world name",
                        "&8Current world: &7" + world
                ).build()).withListener(inventoryClickEvent -> {
            /*final World[] worldPrompt = new World[1];
            do{
                ChatPromptUtils.showPrompt(plugin, player, "&6Please enter the new name of world.", chatConfirmEvent -> {
                    worldPrompt[0] = Bukkit.getWorld(chatConfirmEvent.getMessage());
                });
            }while(worldPrompt[0] != null);*/
        });
        ZButton acceptButton = new ZButton(new ItemBuilder(XMaterial.valueOf("EMERALD_BLOCK").parseMaterial())
                .name("&aCreate arena")
                .lore(
                        "&7Click here to",
                        "&7create arena."
                ).build()).withListener(inventoryClickEvent -> {
            Arena arena = new Arena(arenaName, loc1, loc2);
            plugin.arenaConfig.set("arenas." + arenaName + ".loc1", LocationUtils.unparseLocToString(loc1));
            plugin.arenaConfig.set("arenas." + arenaName + ".loc2", LocationUtils.unparseLocToString(loc2));
            plugin.arenaConfig.set("arenas." + arenaName + ".world", world);

            plugin.saveArenaConfig();
            plugin.getArenaManager().addArena(arena);
            player.closeInventory();
            MessageUtils.sendMessage(player, "&6Successful create arena: &e" + arenaName);
        });
        createArenaGui.setButton(0, renameButton);
        createArenaGui.setButton(1, loc1Button);
        createArenaGui.setButton(2, loc2Button);
        createArenaGui.setButton(3, worldButton);
        createArenaGui.setButton(8, acceptButton);
        return createArenaGui.getInventory();
    }
}
