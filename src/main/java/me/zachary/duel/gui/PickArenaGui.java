package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PickArenaGui {
    private Duel plugin;

    public PickArenaGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getPickArenaGui(Player requested, Player requester){
        ZMenu pickArenaGui = Duel.getGUI().create(plugin.getMessageManager().getString("Gui.PickArena.Name"), 6);
        pickArenaGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for(Arena arena : plugin.getArenaManager().getArenas()){
            if(arena.isStarted() || !requester.hasPermission("duel.arena." + arena.getArenaName())) continue;
            ZButton arenaButton = new ZButton(new ItemBuilder(XMaterial.valueOf(arena.getMaterial().name()).parseMaterial())
            .name("&9" + arena.getArenaName())
            .build()).withListener(inventoryClickEvent -> {
                requester.closeInventory();
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
                        requester.openInventory(new BetGui(plugin).getBetGui(requester, arena,0, "bet2"));
                        requested.closeInventory();
                        requested.openInventory(new BetGui(plugin).getBetGui(requested, arena, 0, "bet1"));
                    });
                }, () -> {
                    plugin.players.remove(requested);
                    MessageUtils.sendMessage(requester, plugin.getMessageManager().getString("Duel deny").replace("{player}", requested.getName()));
                    requested.closeInventory();
                }));
                pickArenaGui.setOnClose(zMenu -> {});
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    MessageUtils.sendMessage(requester, plugin.getMessageManager().getString("Duel request send").replace("{player}", requested.getName()));
                });
            });
            pickArenaGui.setButton(slot, arenaButton);
            slot++;
        }
        ZButton cancelButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.PickArena.Cancel.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.PickArena.Cancel.Name")).build()).withListener(inventoryClickEvent -> {
            pickArenaGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
                plugin.players.remove(requested);
            });
        });
        pickArenaGui.setButton(53, cancelButton);
        pickArenaGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                requester.openInventory(zMenu.getInventory());
            });
        });

        return pickArenaGui.getInventory();
    }
}
