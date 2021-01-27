package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.kits.Kit;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.MessageUtils;
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

    public Inventory getKitGui(Player requester, Player requested, Arena arena){
        ZMenu kitGui = Duel.getGUI().create(plugin.getMessageManager().getString("Gui.Kit.Name"), 1);
        kitGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for (Kit kit : plugin.getKitManager().getKits()) {
            ZButton kitButton = new ZButton(new ItemBuilder(kit.getIcon())
            .name(kit.getKitName()).build()).withListener(inventoryClickEvent -> {
                kit(requested, requester, arena, kit);
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
            kit(requested, requester, arena, null);
            kitGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
        });
        if(plugin.getConfig().getBoolean("Duel with own items"))
            kitGui.setButton(8, noKitButton);
        kitGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                requested.openInventory(zMenu.getInventory());
            });
        });

        return kitGui.getInventory();
    }

    public void kit(Player requester, Player requested, Arena arena, Kit kit){
        MessageUtils.sendMessage(requester, plugin.getMessageManager().getString("Duel request send").replace("{player}", requested.getName()));
        requested.openInventory(new ConfirmGui(plugin).getConfirmGui(requested,
                plugin.getMessageManager().getString("Gui.Confirm duel.Name").replace("{player}", requester.getName()),
                plugin.getMessageManager().getString("Gui.Confirm duel.Confirm.Name"),
                plugin.getMessageManager().getString("Gui.Confirm duel.Cancel.Name"),
                plugin.getMessageManager().getString("Gui.Confirm duel.Confirm.Lore"),
                plugin.getMessageManager().getString("Gui.Confirm duel.Cancel.Lore"), () -> {
                    plugin.players.remove(requested);
                    MessageUtils.sendMessage(requester, plugin.getMessageManager().getString("Duel accept").replace("{player}", requested.getName()));
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        requester.closeInventory();
                        requester.openInventory(new BetGui(plugin).getBetGui(requester, arena,0, "bet2", kit));
                        requested.closeInventory();
                        requested.openInventory(new BetGui(plugin).getBetGui(requested, arena, 0, "bet1", kit));
                    });
                }, () -> {
                    plugin.players.remove(requested);
                    MessageUtils.sendMessage(requester, plugin.getMessageManager().getString("Duel deny").replace("{player}", requested.getName()));
                    requested.closeInventory();
                }));
    }
}
