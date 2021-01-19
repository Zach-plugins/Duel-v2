package me.zachary.duel.utils;

import me.zachary.duel.Duel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LoreUtils {
    private static final Duel plugin = JavaPlugin.getPlugin(Duel.class);

    public static List<String> getLore(String path, String oldChar1, String newChar1) {
        return getLore(path, oldChar1, newChar1, "", "");
    }

    public static List<String> getLore(String path, String oldChar1, String newChar1, String oldChar2, String newChar2) {
        List<String> lore = new ArrayList<>();
        for (String l : plugin.getMessageManager().getStringList(path)) {
            lore.add(l.replace(oldChar1, String.valueOf(newChar1)).replace(oldChar2, String.valueOf(newChar2)));
        }
        return lore;
    }
}
