package me.zachary.duel;

import me.zachary.duel.commands.TestCommand;
import me.zachary.zachcore.ZachCorePlugin;
import me.zachary.zachcore.guis.ZachGUI;

public final class Duel extends ZachCorePlugin {
    public static ZachGUI zachGUI;

    @Override
    public void onEnable() {
        zachGUI = new ZachGUI(this);

        new TestCommand(this);

        preEnable();
    }

    @Override
    public void onDisable() {

    }

    public static ZachGUI getGUI() {
        return zachGUI;
    }
}
