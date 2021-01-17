package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfirmGui {
    private Duel plugin;

    public ConfirmGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getConfirm(Player player, String guiName, String confirmButtonLore, String denyButtonLore, Runnable runnable) {
        ZMenu confirmGui = Duel.getGUI().create(guiName, 3);
        confirmGui.setAutomaticPaginationEnabled(false);

        ZButton confirmButton = new ZButton(new ItemBuilder(XMaterial.valueOf("EMERALD_BLOCK").parseMaterial())
                .name("&aConfirm")
                .lore(confirmButtonLore).build()).withListener(inventoryClickEvent -> {
                    runnable.run();
                    player.closeInventory();
        });

        ZButton denyButton = new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE_BLOCK").parseMaterial())
                .name("&cCancel")
                .lore(denyButtonLore).build()).withListener(inventoryClickEvent -> {
                    player.closeInventory();
        });

        confirmGui.setButton(12, confirmButton);
        confirmGui.setButton(14, denyButton);
        return confirmGui.getInventory();
    }
}
