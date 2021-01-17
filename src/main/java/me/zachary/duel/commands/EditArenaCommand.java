package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.duel.gui.EditArenaGui;
import me.zachary.zachcore.commands.SubCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class EditArenaCommand extends SubCommand {
    private Duel plugin;

    public EditArenaCommand(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        player.openInventory(new EditArenaGui(plugin).getEditArenaGui());
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender consoleCommandSender, String[] strings) {}

    @Override
    public String getName() {
        return "editarena";
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
