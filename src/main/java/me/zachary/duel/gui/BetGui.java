package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.kits.Kit;
import me.zachary.duel.utils.LoreUtils;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.utils.ChatPromptUtils;
import me.zachary.zachcore.utils.MessageUtils;
import me.zachary.zachcore.utils.NumberUtils;
import me.zachary.zachcore.utils.hooks.EconomyManager;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BetGui {
    private Duel plugin;

    public BetGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getBetGui(Player player, Arena arena, double playerBet, String bet, Kit kit){
        ZMenu betGui = Duel.getGUI().create(plugin.getMessageManager().getString("Gui.Bet.Name"), 1);
        betGui.setAutomaticPaginationEnabled(false);

        ZButton onekButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Bet.1k.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.Bet.1k.Name"))
                .lore(plugin.getMessageManager().getString("Gui.Bet.1k.Lore"))
                .build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            Bukkit.getScheduler().runTask(plugin, () -> {
                if(EconomyManager.getBalance(player) >= playerBet + 1000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 1000, bet, kit));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                    MessageUtils.sendMessage(player, plugin.getMessageManager().getString("Gui.Bet.Not enough money"));
                }
            });
        });

        ZButton tenkButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Bet.10k.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.Bet.10k.Name"))
                .lore(plugin.getMessageManager().getString("Gui.Bet.10k.Lore"))
                .build()).withListener(inventoryClickEvent -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                betGui.setOnClose(zMenu -> {});
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                });
                if(EconomyManager.getBalance(player) >= playerBet + 10000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 10000, bet, kit));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                    MessageUtils.sendMessage(player, plugin.getMessageManager().getString("Gui.Bet.Not enough money"));
                }
            });
        });

        ZButton hundredkButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Bet.100k.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.Bet.100k.Name"))
                .lore(plugin.getMessageManager().getString("Gui.Bet.100k.Lore"))
                .build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            Bukkit.getScheduler().runTask(plugin, () -> {
                if(EconomyManager.getBalance(player) >= playerBet + 100000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 100000, bet, kit));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                    MessageUtils.sendMessage(player, plugin.getMessageManager().getString("Gui.Bet.Not enough money"));
                }
            });
        });

        ZButton customButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Bet.Custom.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.Bet.Custom.Name"))
                .lore(plugin.getMessageManager().getString("Gui.Bet.Custom.Lore"))
                .build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            ChatPromptUtils.showPrompt(plugin, (Player) inventoryClickEvent.getWhoClicked(), plugin.getMessageManager().getString("Gui.Bet.Custom.Question").replace("{balance}", String.valueOf((EconomyManager.getBalance((Player) inventoryClickEvent.getWhoClicked()) - playerBet))), chatConfirmEvent -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if(NumberUtils.tryParseDouble(chatConfirmEvent.getMessage())){
                        if(EconomyManager.getBalance(player) >= playerBet + Double.parseDouble(chatConfirmEvent.getMessage()))
                            inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + Double.parseDouble(chatConfirmEvent.getMessage()), bet, kit));
                        else{
                            inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                            MessageUtils.sendMessage(player, plugin.getMessageManager().getString("Gui.Bet.Not enough money"));
                        }
                    }else{
                        inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                    }
                });
            }).setTimeOut((Player) inventoryClickEvent.getWhoClicked(), 100, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet, kit));
                });
            });
        });

        ZButton acceptButton = new ZButton(new ItemBuilder(XMaterial.valueOf(plugin.getMessageManager().getString("Gui.Bet.Accept.Material")).parseMaterial())
                .name(plugin.getMessageManager().getString("Gui.Bet.Accept.Name"))
                .lore(LoreUtils.getLore("Gui.Bet.Accept.Lore", "{bet}", String.valueOf(playerBet)))
                .build()).withListener(inventoryClickEvent -> {
            EconomyManager.withdrawBalance(player, playerBet);
            if(bet.equalsIgnoreCase("bet1"))
                plugin.bet1.put(player, playerBet);
            else if(bet.equalsIgnoreCase("bet2")){
                plugin.bet2.put(player, playerBet);
            }
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            plugin.getArenaManager().joinArena(player, arena, kit);
        });

        betGui.setButton(0, onekButton);
        betGui.setButton(1, tenkButton);
        betGui.setButton(2, hundredkButton);
        betGui.setButton(3, customButton);
        betGui.setButton(8, acceptButton);
        betGui.setOnClose(zMenu -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.openInventory(zMenu.getInventory());
            });
        });

        return betGui.getInventory();
    }
}
