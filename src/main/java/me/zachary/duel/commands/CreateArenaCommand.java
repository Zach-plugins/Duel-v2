package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.utils.LocationUtils;
import me.zachary.zachcore.commands.SubCommand;
import me.zachary.zachcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
        Arena arena = new Arena(strings[2], Material.valueOf("GRASS_BLOCK"), loc1, loc2);
        plugin.arenaConfig.set("arenas." + strings[2] + ".loc1", LocationUtils.unparseLocToString(loc1));
        plugin.arenaConfig.set("arenas." + strings[2] + ".loc2", LocationUtils.unparseLocToString(loc2));
        plugin.arenaConfig.set("arenas." + strings[2] + ".material", "GRASS_BLOCK");
        plugin.arenaConfig.set("arenas." + strings[2] + ".world", world);

        plugin.saveArenaConfig();
        plugin.getArenaManager().addArena(arena);
        MessageUtils.sendMessage(player, "&6Successful create arena: &e" + strings[2]);
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
