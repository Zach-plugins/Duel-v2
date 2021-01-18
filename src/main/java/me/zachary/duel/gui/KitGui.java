package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.kits.Kit;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitGui {
    private Duel plugin;

    public KitGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getKitGui(Player player, Arena arena){
        ZMenu kitGui = Duel.getGUI().create("&6Select a kit", 1);
        kitGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for (Kit kit : plugin.getKitManager().getKits()) {
            ZButton kitButton = new ZButton(new ItemBuilder(kit.getIcon())
            .name(kit.getKitName()).build()).withListener(inventoryClickEvent -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
                plugin.getArenaManager().joinArena(player, arena, kit);
            });
            kitGui.setButton(slot, kitButton);
            slot++;
        }

        return kitGui.getInventory();
    }
}
