package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.kits.Kit;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitGui {
    private Duel plugin;

    public KitGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getKitGui(Player player, Arena arena){
        ZMenu kitGui = Duel.getGUI().create(plugin.getMessageManager().getString("Gui.Kit.Name"), 1);
        kitGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for (Kit kit : plugin.getKitManager().getKits()) {
            ZButton kitButton = new ZButton(new ItemBuilder(kit.getIcon())
            .name(kit.getKitName()).build()).withListener(inventoryClickEvent -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
                plugin.getArenaManager().joinArena(player, arena, kit);
                kitGui.setOnClose(zMenu -> {});
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                });
            });
            kitGui.setButton(slot, kitButton);
            slot++;
        }
        ZButton noKitButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Kit.noKit.Material")).parseMaterial())
        .name(plugin.getMessageManager().getString("Gui.Kit.noKit.Name"))
        .lore(plugin.getMessageManager().getStringList("Gui.Kit.noKit.Lore"))
        .build()).withListener(inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().closeInventory();
            plugin.getArenaManager().joinArena(player, arena, null);
            kitGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
        });
        if(plugin.getConfig().getBoolean("Duel with own items"))
            kitGui.setButton(8, noKitButton);
        kitGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.openInventory(zMenu.getInventory());
            });
        });

        return kitGui.getInventory();
    }
}
