package me.zachary.duel;

import me.zachary.duel.commands.CommandManager;
import me.zachary.zachcore.ZachCorePlugin;
import me.zachary.zachcore.guis.ZachGUI;

public final class Duel extends ZachCorePlugin {
    public static ZachGUI zachGUI;

    @Override
    public void onEnable() {
        zachGUI = new ZachGUI(this);

        new CommandManager(this);

        preEnable();
    }

    @Override
    public void onDisable() {

    }

    public static ZachGUI getGUI() {
        return zachGUI;
    }
}
