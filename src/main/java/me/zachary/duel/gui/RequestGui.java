package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.utils.LoreUtils;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.guis.pagination.ZPaginationButtonBuilder;
import me.zachary.zachcore.utils.ChatPromptUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.SkullUtils;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class RequestGui {
    private Duel plugin;

    public RequestGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getRequestGUI(Player player, String searchName){
        ZMenu requestGui = Duel.getGUI().create(plugin.getMessageManager().getString("Gui.Request.Name"), 5);
        requestGui.setPaginationButtonBuilder(getPaginationButtonBuilder(player));
        setGlass(requestGui, 0);

        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        int slot = 10;
        int page = 0;
        for (Player p : players) {
            if(p == player
                    || plugin.players.containsKey(p)
                    || plugin.getArenaManager().getArenaByPlayer(p) != null) continue;
            if(searchName != null)
                if(!p.getName().toLowerCase().startsWith(searchName.toLowerCase())) continue;
            ItemBuilder skullItem = new ItemBuilder(SkullUtils.getSkull(p.getUniqueId()))
                    .name("&e" + p.getName())
                    .lore(LoreUtils.getLore("Gui.Request.Lore",
                            "{win}", String.valueOf(plugin.getDatabaseManager().getPlayerWin().get(p)),
                            "{lose}", String.valueOf(plugin.getDatabaseManager().getPlayerLose().get(p))));
            ZButton skull = new ZButton(skullItem.build()).withListener(inventoryClickEvent -> {
                plugin.players.put(p, player);
                player.closeInventory();
                player.openInventory(new PickArenaGui(plugin).getPickArenaGui(p, player));
            });

            requestGui.setButton(page, slot, skull);
            slot++;
            if(slot == 35){
                slot = 0;
                page++;
            }
            int finalPage = page;
            setGlass(requestGui, finalPage);
        }
        return requestGui.getInventory();
    }

    public ZPaginationButtonBuilder getPaginationButtonBuilder(Player player){
        return (type, inventory) -> {
            switch (type) {
                case CLOSE_BUTTON:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Close.Material")).parseMaterial())
                            .name(plugin.getMessageManager().getString("Gui.Request.Close.Name"))
                            .build()
                    ).withListener(event -> {
                        event.getWhoClicked().closeInventory();
                    });

                case PREV_BUTTON:
                    if (inventory.getCurrentPage() > 0) return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Previous.Material")).parseMaterial())
                            .name(plugin.getMessageManager().getString("Gui.Request.Previous.Name"))
                            .lore(LoreUtils.getLore("Gui.Request.Previous.Lore", "{page}", String.valueOf(inventory.getCurrentPage())))
                            .build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.previousPage(event.getWhoClicked());
                    });
                    else return null;

                case CURRENT_BUTTON:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Current.Material")).parseMaterial())
                            .name(plugin.getMessageManager().getString("Gui.Request.Current.Name").replace("{currentpage}", String.valueOf(inventory.getCurrentPage() + 1)).replace("{maxpage}", String.valueOf(inventory.getMaxPage())))
                            .lore(LoreUtils.getLore("Gui.Request.Current.Lore", "{page}", String.valueOf(inventory.getCurrentPage() + 1)))
                            .build()
                    ).withListener(event -> event.setCancelled(true));

                case NEXT_BUTTON:
                    if (inventory.getCurrentPage() < inventory.getMaxPage() - 2) return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Next.Material")).parseMaterial())
                            .name(plugin.getMessageManager().getString("Gui.Request.Next.Name"))
                            .lore(LoreUtils.getLore("Gui.Request.Next.Lore", "{page}", String.valueOf(inventory.getCurrentPage() + 2)))
                            .build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.nextPage(event.getWhoClicked());
                    });
                    else return null;

                case CUSTOM_2:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Search.Material")).parseMaterial())
                            .name(plugin.getMessageManager().getString("Gui.Request.Search.Name"))
                            .lore(plugin.getMessageManager().getStringList("Gui.Request.Search.Lore")).build()
                    ).withListener(inventoryClickEvent -> {
                        ChatPromptUtils.showPrompt(plugin, (Player) inventoryClickEvent.getWhoClicked(), plugin.getMessageManager().getString("Gui.Request.Search.Question"), chatConfirmEvent -> {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                inventoryClickEvent.getWhoClicked().openInventory(getRequestGUI((Player) inventoryClickEvent.getWhoClicked(), chatConfirmEvent.getMessage()));
                            });
                        });
                    });
                case CUSTOM_1:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Request.Stats.Material")).parseMaterial())
                    .name(plugin.getMessageManager().getString("Gui.Request.Stats.Name"))
                    .lore(LoreUtils.getLore("Gui.Request.Stats.Lore",
                            "{win}", String.valueOf(plugin.getDatabaseManager().getPlayerWin().get(player)),
                            "{lose}", String.valueOf(plugin.getDatabaseManager().getPlayerLose().get(player))))
                    .build());
                case CUSTOM_3:
                case CUSTOM_4:
                case UNASSIGNED:
                default:
                    return null;
            }
        };
    }

    public void setGlass(ZMenu menu, int page) {
        int[] TILES_TO_UPDATE = {
                0,  1,  2,  3,  4,  5,  6,  7,  8,
                9,                             17,
                18,                            26,
                27,                            35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        };
        IntStream.range(0, TILES_TO_UPDATE.length).map(i -> TILES_TO_UPDATE.length - i + -1).forEach(
                index -> menu.setButton(page, TILES_TO_UPDATE[index], new ZButton(new ItemBuilder(XMaterial.valueOf("GREEN_STAINED_GLASS_PANE").parseMaterial()).build()))
        );
    }
}
