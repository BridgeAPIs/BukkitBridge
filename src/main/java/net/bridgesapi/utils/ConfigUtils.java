package net.bridgesapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author created by ThisIsMac on 26/05/2015.
 */
public class ConfigUtils {

    /**
     * Parse string to location
     * @param loc String that will be deserialized
     * @return deserialized Location
     */
    public static Location stringToLocation(String loc) {
        if (loc == null)
            return null;
        String[] loca = loc.split(", ");

        return new Location(Bukkit.getServer().getWorld(loca[0]), Double.parseDouble(loca[1]), Double.parseDouble(loca[2]), Double.parseDouble(loca[3]), Float.parseFloat(loca[4]), Float.parseFloat(loca[5]));
    }

    /**
     * Parse location to string
     * @param loc Location that will be serialized
     * @return serialized String
     */
    public static String locationToString(Location loc) {
        return String.format("%s, %d, %d, %d, %f.2, %f.2", loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch());
    }



}
