package me.zachary.duel.commands;

import me.zachary.zachcore.commands.SubCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        player.sendMessage("Help");
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender consoleCommandSender, String[] strings) {}

    @Override
    public String getName() {
        return "help";
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
