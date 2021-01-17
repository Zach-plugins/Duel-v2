package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.gui.CreateArenaGui;
import me.zachary.duel.utils.LocationUtils;
import me.zachary.zachcore.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CreateArenaCommand extends SubCommand {
    private Duel plugin;

    public CreateArenaCommand(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommandByPlayer(Player player, String[] strings) {
        if (strings.length < 3) {
            player.sendMessage("/duel createarena <loc1> <loc2> <ArenaName>");
            return;
        }
        String world = player.getLocation().getWorld().getName();
        Location loc1 = LocationUtils.parseStringToLoc(strings[0], world);
        Location loc2 = LocationUtils.parseStringToLoc(strings[1], world);
        if(loc1 == null || loc2 == null){
            player.sendMessage("/duel createarena X,Y,Z X,Y,Z <ArenaName>");
            return;
        }
        player.openInventory(new CreateArenaGui(JavaPlugin.getPlugin(Duel.class)).getCreateArenaGui(player, loc1, loc2, strings[2], world));
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender consoleCommandSender, String[] strings) {}

    @Override
    public String getName() {
        return "createarena";
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
