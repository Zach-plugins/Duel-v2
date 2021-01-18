package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
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

    public Inventory getBetGui(Player player, Arena arena, double playerBet, String bet){
        ZMenu betGui = Duel.getGUI().create("&6Make your bet", 1);
        betGui.setAutomaticPaginationEnabled(false);

        ZButton onekButton = new ZButton(new ItemBuilder(XMaterial.valueOf("GOLD_NUGGET").parseMaterial())
                .name("&6Add 1k")
                .lore(
                        "&7Click to add &e1000$"
                ).build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            Bukkit.getScheduler().runTask(plugin, () -> {
                if(EconomyManager.getBalance(player) >= playerBet + 1000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 1000, bet));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                    MessageUtils.sendMessage(player, "&cYou don't have enough money to do that.");
                }
            });
        });

        ZButton tenkButton = new ZButton(new ItemBuilder(XMaterial.valueOf("GOLD_INGOT").parseMaterial())
                .name("&6Add 10k")
                .lore(
                        "&7Click to add &e10000$"
                ).build()).withListener(inventoryClickEvent -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                betGui.setOnClose(zMenu -> {});
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                });
                if(EconomyManager.getBalance(player) >= playerBet + 10000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 10000, bet));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                    MessageUtils.sendMessage(player, "&cYou don't have enough money to do that.");
                }
            });
        });

        ZButton hundredkButton = new ZButton(new ItemBuilder(XMaterial.valueOf("GOLD_NUGGET").parseMaterial())
                .name("&6Add 100k")
                .lore(
                        "&7Click to add &e100000$"
                ).build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            Bukkit.getScheduler().runTask(plugin, () -> {
                if(EconomyManager.getBalance(player) >= playerBet + 100000)
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + 100000, bet));
                else{
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                    MessageUtils.sendMessage(player, "&cYou don't have enough money to do that.");
                }
            });
        });

        ZButton customButton = new ZButton(new ItemBuilder(XMaterial.valueOf("ANVIL").parseMaterial())
                .name("&6Custom amount")
                .lore(
                        "&7Click to enter a custom amount"
                ).build()).withListener(inventoryClickEvent -> {
            betGui.setOnClose(zMenu -> {});
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventoryClickEvent.getWhoClicked().closeInventory();
            });
            ChatPromptUtils.showPrompt(plugin, (Player) inventoryClickEvent.getWhoClicked(), "&6Enter a custom money amount. You have 5 seconds. Balance: &e" + (EconomyManager.getBalance((Player) inventoryClickEvent.getWhoClicked()) - playerBet) + "&6$", chatConfirmEvent -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if(NumberUtils.tryParseDouble(chatConfirmEvent.getMessage())){
                        if(EconomyManager.getBalance(player) >= playerBet + Double.parseDouble(chatConfirmEvent.getMessage()))
                            inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet + Double.parseDouble(chatConfirmEvent.getMessage()), bet));
                        else{
                            inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                            MessageUtils.sendMessage(player, "&cYou don't have enough money to do that.");
                        }
                    }else{
                        inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                    }
                });
            }).setTimeOut((Player) inventoryClickEvent.getWhoClicked(), 100, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventoryClickEvent.getWhoClicked().openInventory(getBetGui(player, arena, playerBet, bet));
                });
            });
        });

        ZButton acceptButton = new ZButton(new ItemBuilder(XMaterial.valueOf("PAPER").parseMaterial())
                .name("&6Accept your bet")
                .lore(
                        "&7Your bet: &8" + playerBet + "&7$",
                        "&7Winner will win all money bet by",
                        "&7the 2 players."
                ).build()).withListener(inventoryClickEvent -> {
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
            player.openInventory(new KitGui(plugin).getKitGui(player, arena));
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
