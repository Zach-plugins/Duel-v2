package me.zachary.duel.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {
    public static Location parseStringToLoc(String location, String world) {
        String[] parsedLoc = location.split(",");
        if(parsedLoc.length < 3)
            return null;
        double x = Double.valueOf(parsedLoc[0]);
        double y = Double.valueOf(parsedLoc[1]);
        double z = Double.valueOf(parsedLoc[2]);

        return new Location(Bukkit.getWorld(world), x,y,z);
    }

    public static String unparseLocToString(Location loc) {
        return loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }
}
