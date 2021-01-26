package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.zachcore.commands.SubCommand;
import me.zachary.zachcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {
    private Duel plugin;

    public ReloadCommand(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        plugin.getKitManager().reloadKit();
        plugin.getArenaManager().reloadArena();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        MessageUtils.sendMessage(player, "&cYou have successful reload the config.");
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender consoleCommandSender, String[] strings) {
        plugin.getKitManager().reloadKit();
        plugin.getArenaManager().reloadArena();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        Bukkit.getConsoleSender().sendMessage("§cYou have successful reload the config.");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
