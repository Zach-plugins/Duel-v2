package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.duel.gui.RequestGui;
import me.zachary.zachcore.commands.SubCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RequestCommand extends SubCommand {
    private Duel plugin;

    public RequestCommand(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        player.openInventory(new RequestGui(plugin).getRequestGUI(player, null));
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender consoleCommandSender, String[] strings) {}

    @Override
    public String getName() {
        return "request";
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
