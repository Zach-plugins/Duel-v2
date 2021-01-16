package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AcceptGui {
    private Duel plugin;

    public AcceptGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getAcceptGui(Player requester){
        ZMenu acceptGui = Duel.getGUI().create("&a&lAccept &7or &c&ldeny &7the duel from " + requester.getName() + "!", 3);
        acceptGui.setAutomaticPaginationEnabled(false);
        ZButton acceptButton = new ZButton(new ItemBuilder(XMaterial.valueOf("EMERALD_BLOCK").parseMaterial())
                .name("&aAccept the duel!")
                .lore(
                        "&7Click here to accept",
                        "&7from " + requester.getName()
                ).build());

        ZButton denyButton = new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE_BLOCK").parseMaterial())
                .name("&cDeny the duel!")
                .lore(
                        "&7Click here to deny",
                        "&7from " + requester.getName()
                ).build());

        acceptGui.setButton(12, acceptButton);
        acceptGui.setButton(14, denyButton);
        return acceptGui.getInventory();
    }
}
