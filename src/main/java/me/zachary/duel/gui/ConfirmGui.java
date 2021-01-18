package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ConfirmGui {
    private Duel plugin;

    public ConfirmGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getConfirmGui(Player player, String guiName, String confirmButtonName, String cancelButtonName, String confirmButtonLore, String denyButtonLore, Runnable runnableConfirm, Runnable runnableCancel) {
        ZMenu confirmGui = Duel.getGUI().create(guiName, 3);
        confirmGui.setAutomaticPaginationEnabled(false);

        ZButton confirmButton = new ZButton(new ItemBuilder(XMaterial.valueOf("EMERALD_BLOCK").parseMaterial())
                .name(confirmButtonName)
                .lore(confirmButtonLore).build()).withListener(inventoryClickEvent -> {
                    if(runnableConfirm != null)
                        runnableConfirm.run();
                    player.closeInventory();
            confirmGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
        });

        ZButton denyButton = new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE_BLOCK").parseMaterial())
                .name(cancelButtonName)
                .lore(denyButtonLore).build()).withListener(inventoryClickEvent -> {
                    if(runnableCancel != null)
                        runnableCancel.run();
                    player.closeInventory();
            confirmGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
        });

        confirmGui.setButton(12, confirmButton);
        confirmGui.setButton(14, denyButton);
        confirmGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.openInventory(zMenu.getInventory());
            });
        });
        return confirmGui.getInventory();
    }
}
