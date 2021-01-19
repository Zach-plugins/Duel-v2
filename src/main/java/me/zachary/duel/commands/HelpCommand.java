package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.zachcore.commands.SubCommand;
import me.zachary.zachcore.utils.MessageUtils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    private Duel plugin;

    public HelpCommand(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        MessageUtils.sendMessage(player, plugin.getMessageManager().getStringList("Help"));
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
