package me.zachary.duel;

import me.zachary.zachcore.storage.YAMLFile;
import me.zachary.zachcore.utils.hooks.EconomyManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private Duel plugin;
    private YAMLFile cfg;

    public MessageManager(Duel plugin) {
        this.plugin = plugin;
    }

    public void load(){
        this.cfg = new YAMLFile(plugin.getDataFolder(), "message.yml");
        this.loadDefaults();
    }

    private void loadDefaults() {
        ArrayList<String> lore = new ArrayList<String>();
        this.cfg.getConfig().options().header("Message file");
        this.cfg.getConfig().options().copyHeader(true);
        this.cfg.add("No permission", "&cYou don't have permission to do that.");
        this.cfg.add("Unknown arguments", "&cUnknown arguments!");
        this.cfg.add("Duel lose", "&6You lose the duel.");
        this.cfg.add("Duel win", "&6You win the duel.");
        this.cfg.add("Duel broadcast win", "&e{winner} &6won the duel against &e{loser}&6!");
        this.cfg.add("Duel leave", "&e{leaver} &6leave the server. So, you win the duel.");
        this.cfg.add("Duel start", "&6Your duel start!");
        this.cfg.add("Duel request send", "&6Successful send a request duel to &e{player}");
        this.cfg.add("Duel accept", "&2{player} &aaccept duel request!");
        this.cfg.add("Duel deny", "&4{player} &cdeny duel request!");
        /* Bet Gui */
        this.cfg.add("Gui.Bet.Name", "&6Make your bet");
        this.cfg.add("Gui.Bet.Not enough money", "&cYou don't have enough money to do that.");
        this.cfg.add("Gui.Bet.1k.Material", "GOLD_NUGGET");
        this.cfg.add("Gui.Bet.1k.Name", "&6Add 1k");
        this.cfg.add("Gui.Bet.1k.Lore", "&7Click to add &e1000$");
        this.cfg.add("Gui.Bet.10k.Material", "GOLD_INGOT");
        this.cfg.add("Gui.Bet.10k.Name", "&6Add 10k");
        this.cfg.add("Gui.Bet.10k.Lore", "&7Click to add &e10000$");
        this.cfg.add("Gui.Bet.100k.Material", "GOLD_BLOCK");
        this.cfg.add("Gui.Bet.100k.Name", "&6Add 100k");
        this.cfg.add("Gui.Bet.100k.Lore", "&7Click to add &e100000$");
        this.cfg.add("Gui.Bet.Custom.Material", "ANVIL");
        this.cfg.add("Gui.Bet.Custom.Name", "&6Custom amount");
        this.cfg.add("Gui.Bet.Custom.Lore", "&7Click to enter a custom amount");
        this.cfg.add("Gui.Bet.Custom.Question", "&6Enter a custom money amount. You have 5 seconds. Balance: &e{balance}&6$");
        this.cfg.add("Gui.Bet.Accept.Material", "EMERALD_BLOCK");
        this.cfg.add("Gui.Bet.Accept.Name", "&6Accept your bet");
        lore.add("&7Your bet: &8{bet}&7$");
        lore.add("&7Winner will win all money bet by");
        lore.add("&7the 2 players.");
        this.cfg.add("Gui.Bet.Accept.Lore", lore.toArray());
        lore.clear();
        /* Kit Gui */
        this.cfg.add("Gui.Kit.Name", "&6Select a kit");
        this.cfg.add("Gui.Kit.noKit.Material", "GLASS");
        this.cfg.add("Gui.Kit.noKit.Name", "&6Your current item");
        lore.add("&7This allow you to duel");
        lore.add("&7with your own item.");
        this.cfg.add("Gui.Kit.noKit.Lore", lore.toArray());
        lore.clear();
        /* PickArena Gui */
        this.cfg.add("Gui.PickArena.Name", "&6Choice an arena for duel");
        this.cfg.add("Gui.PickArena.Cancel.Material", "REDSTONE_BLOCK");
        this.cfg.add("Gui.PickArena.Cancel.Name", "&cClick to cancel");
        /* Confirm duel Gui */
        this.cfg.add("Gui.Confirm duel.Name", "&6Duel request from &e{player}");
        this.cfg.add("Gui.Confirm duel.Confirm.Name", "&aAccept the duel");
        this.cfg.add("Gui.Confirm duel.Confirm.Lore", "&7Click here to accept.");
        this.cfg.add("Gui.Confirm duel.Cancel.Name", "&cDeny the duel");
        this.cfg.add("Gui.Confirm duel.Cancel.Lore", "&7Click here to deny.");
        /* Request Gui */
        this.cfg.add("Gui.Request.Name", "&6&lRequest duel");
        lore.add("&2Wins: {win}");
        lore.add("&cLoses: {lose}");
        this.cfg.add("Gui.Request.Lore", lore.toArray());
        lore.clear();
        this.cfg.add("Gui.Request.Close.Material", "REDSTONE");
        this.cfg.add("Gui.Request.Close.Name", "&c&lClose menu");
        this.cfg.add("Gui.Request.Previous.Material", "ARROW");
        this.cfg.add("Gui.Request.Previous.Name", "&a&l\u2190 Previous Page");
        lore.add("&aClick to move back to");
        lore.add("&apage {page}.");
        this.cfg.add("Gui.Request.Previous.Lore", lore.toArray());
        lore.clear();
        this.cfg.add("Gui.Request.Current.Material", "NAME_TAG");
        this.cfg.add("Gui.Request.Current.Name", "&7&lPage &8{currentpage} &7of &8{maxpage}");
        lore.add("&7You are currently viewing");
        lore.add("&7page {page}.");
        this.cfg.add("Gui.Request.Current.Lore", lore.toArray());
        lore.clear();
        this.cfg.add("Gui.Request.Next.Material", "ARROW");
        this.cfg.add("Gui.Request.Next.Name", "&a&lNext Page \u2192");
        lore.add("&aClick to move forward to");
        lore.add("&apage {page}.");
        this.cfg.add("Gui.Request.Next.Lore", lore.toArray());
        lore.clear();
        this.cfg.add("Gui.Request.Search.Material", "COMPASS");
        this.cfg.add("Gui.Request.Search.Name", "&6Search a player name.");
        lore.add("&aClick to search a");
        lore.add("&aspecific player name.");
        this.cfg.add("Gui.Request.Search.Lore", lore.toArray());
        lore.clear();
        this.cfg.add("Gui.Request.Search.Question", "&6Enter the name of player you want search.");
        this.cfg.add("Gui.Request.Stats.Material", "OAK_SIGN");
        this.cfg.add("Gui.Request.Stats.Name", "&6Your stats");
        lore.add("&2Wins: {win}");
        lore.add("&cLoses: {lose}");
        this.cfg.add("Gui.Request.Stats.Lore", lore.toArray());
        lore.clear();
        lore.add("&7==========================================");
        lore.add("&6/duel &7- &eAsk in duel someone.");
        lore.add("&6/duel createarena <X,Y,Z> <X,Y,Z> <ArenaName> &7- &eCreate an arena.");
        lore.add("&6/duel editarena &7- &eEdit current arena.");
        lore.add("&6/duel reload &7- &eReload config.");
        lore.add("&7==========================================");
        this.cfg.add("Help", lore.toArray());
        lore.clear();
    }

    public String getString(String path){
        return this.cfg.getString(path);
    }

    public List<String> getStringList(String path){
        return this.cfg.getStringList(path);
    }
}
