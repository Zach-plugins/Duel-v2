package me.zachary.duel.commands;

import me.zachary.duel.Duel;
import me.zachary.duel.gui.RequestGui;
import me.zachary.zachcore.commands.SubCommand;
import me.zachary.zachcore.utils.MessageUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private Duel plugin;
    private List<SubCommand> commands;

    public CommandManager(Duel plugin) {
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand("duel");
        command.setExecutor(this);
        command.setTabCompleter(this);
        registerSubCommands();
    }

    private void registerSubCommands() {
        commands = Arrays.asList(
                new HelpCommand(),
                new CreateArenaCommand(plugin),
                new EditArenaCommand(plugin)
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if(command.getName().equalsIgnoreCase("duel")){
            if (sender instanceof Player) {
                player = (Player) sender;
            }

            if(args.length == 0){
                if(player == null){
                    sender.sendMessage("You need to be a player to execute this command.");
                }else{
                    player.openInventory(new RequestGui(plugin).getRequestGUI(player, null));
                }
                return true;
            }

            SubCommand subCommand;
            subCommand = getSubCommand(commands, args[0]);

            if(subCommand == null){
                MessageUtils.sendMessage(player, "&cUnknown arguments!");
                return true;
            }

            if(!player.hasPermission("duel." + subCommand.getName())){
                MessageUtils.sendMessage(player, "&cYou don't have the permission to execute this command.");
                return true;
            }

            List<String> arguments = new ArrayList<>(Arrays.asList(args));
            arguments.remove(args[0]);

            if (sender instanceof Player) {
                subCommand.onCommandByPlayer(player, arguments.toArray(new String[0]));
            } else if (sender instanceof ConsoleCommandSender) {
                subCommand.onCommandByConsole((ConsoleCommandSender) sender,
                        arguments.toArray(new String[0]));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (command.getName().equalsIgnoreCase("duel")){
            List<String> commandAliases = new ArrayList<>();
            if (args.length == 1) {
                if (args[0] == null || args[0].isEmpty()) {
                    for (SubCommand subCommandList : commands) {
                        if(sender.hasPermission("duel." + subCommandList.getName()))
                            commandAliases.add(subCommandList.getName());
                    }
                } else {
                    for (SubCommand subCommandList : commands) {
                        if (subCommandList.getName().toLowerCase().contains(args[0].toLowerCase()) && sender.hasPermission("duel." + subCommandList.getName())) {
                            commandAliases.add(subCommandList.getName());
                        }
                    }
                }
            }

            if (commandAliases.size() != 0) {
                return commandAliases;
            }
        }

        return null;
    }

    public List<String> getArguments(String arg1, String arg2) {
        return this.getArguments(commands, arg1, arg2);
    }

    public List<String> getArguments(List<SubCommand> subCommands, String arg1, String arg2) {
        List<String> arguments = new ArrayList<>();
        for (SubCommand subCommandList : subCommands) {
            if (arg1.equalsIgnoreCase(subCommandList.getName())) {
                if (arg2 == null || arg2.isEmpty()) {
                    arguments.addAll(Arrays.asList(subCommandList.getArguments()));
                } else {
                    for (String argumentList : subCommandList.getArguments()) {
                        if (argumentList.contains(arg2.toLowerCase())) {
                            arguments.add(argumentList);
                            break;
                        }
                    }
                }
                break;
            }
        }

        return arguments;
    }

    public SubCommand getSubCommand(List<SubCommand> subCommands, String cmdName) {
        for (SubCommand command : subCommands) {
            if (command.getName().equalsIgnoreCase(cmdName))
                return command;

            for (String argList : command.getAliases())
                if (argList.equalsIgnoreCase(cmdName))
                    return command;
        }

        return null;
    }
}
