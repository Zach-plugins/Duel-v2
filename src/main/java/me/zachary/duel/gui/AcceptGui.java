package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AcceptGui {
    private Duel plugin;

    public AcceptGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getAcceptGui(Player requested, Player requester){
        ZMenu acceptGui = Duel.getGUI().create("&a&lAccept &7or &c&ldeny &7the duel from " + requester.getName() + "!", 3);
        acceptGui.setAutomaticPaginationEnabled(false);
        ZButton acceptButton = new ZButton(new ItemBuilder(XMaterial.valueOf("EMERALD_BLOCK").parseMaterial())
                .name("&aAccept the duel")
                .lore(
                        "&7Click here to accept"
                ).build()).withListener(inventoryClickEvent -> {
            plugin.players.remove(requested);
            MessageUtils.sendMessage(requester, "&2" + requested.getName() + " accept duel request!");
            requested.closeInventory();
            plugin.getArenaManager().joinArena(requester, requested);
        });

        ZButton denyButton = new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE_BLOCK").parseMaterial())
                .name("&cDeny the duel")
                .lore(
                        "&7Click here to deny"
                ).build()).withListener(inventoryClickEvent -> {
            plugin.players.remove(requested);
            MessageUtils.sendMessage(requester, "&c" + requested.getName() + " deny duel request!");
            requested.closeInventory();
        });

        acceptGui.setButton(12, acceptButton);
        acceptGui.setButton(14, denyButton);
        return acceptGui.getInventory();
    }
}
