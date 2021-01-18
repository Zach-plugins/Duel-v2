package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.hooks.EconomyManager;
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
        ZMenu pickArenaGui = Duel.getGUI().create("&6Choice an arena for duel.", 6);
        pickArenaGui.setAutomaticPaginationEnabled(false);

        int slot = 0;
        for(Arena arena : plugin.getArenaManager().getArenas()){
            if(arena.isStarted()) continue;
            ZButton arenaButton = new ZButton(new ItemBuilder(XMaterial.valueOf(arena.getMaterial().name()).parseMaterial())
            .name("&9" + arena.getArenaName())
            .build()).withListener(inventoryClickEvent -> {
                requester.closeInventory();
                requested.openInventory(new ConfirmGui(plugin).getConfirmGui(requested, "&6Duel request from &e" + requester.getName(), "&aAccept the duel", "&cDeny the duel", "&7Click here to accept.", "&7Click here to deny", () -> {
                    plugin.players.remove(requested);
                    MessageUtils.sendMessage(requester, "&2" + requested.getName() + " accept duel request!");
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        requester.closeInventory();
                        requester.openInventory(new BetGui(plugin).getBetGui(requester, arena,0, "bet2"));
                        requested.closeInventory();
                        requested.openInventory(new BetGui(plugin).getBetGui(requested, arena, 0, "bet1"));
                    });
                }, () -> {
                    plugin.players.remove(requested);
                    MessageUtils.sendMessage(requester, "&c" + requested.getName() + " deny duel request!");
                    requested.closeInventory();
                }));
                pickArenaGui.setOnClose(zMenu -> {});
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                });
            });
            pickArenaGui.setButton(slot, arenaButton);
            slot++;
        }
        pickArenaGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                requester.openInventory(zMenu.getInventory());
            });
        });

        return pickArenaGui.getInventory();
    }
}
