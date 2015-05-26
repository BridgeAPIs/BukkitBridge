package net.bridgesapi.utils;

import org.bukkit.Location;

public class Area
{
    private final Location min = null;
    private final Location max= null;

    /**
     * Create a area from two location (they must not be opposite)
     * @param first first location
     * @param second second location
     */
    public Area(Location first, Location second) {
        build(first, second);
    }

    /**
     * Create Area from one string (use this for loading Area from a config file)
     * @param value config value
     */
    public Area(String value){
        String[] locationStrings = value.split(":");
        build(ConfigUtils.stringToLocation(locationStrings[0]), ConfigUtils.stringToLocation(locationStrings[1]));
    }

    /**
     * Create Area from two string which gonna be sereliaze in Location
     * @param first First string
     * @param second Second string
     */
    public Area(String first, String second) {
        build(ConfigUtils.stringToLocation(first), ConfigUtils.stringToLocation(second));
    }



    /**
     *  Build a area from location (they must not be opposite)
     * @param first first location
     * @param second second location
     */
    public void build(Location first, Location second) {
        Location tempmin = new Location(null, 0, 0, 0);
        Location tempmax = new Location(null, 0, 0, 0);

        if (first == null || second == null)
            return;

        if (first.getX() >= second.getX())
        {
            tempmax.setX(first.getX());
            tempmin.setX(second.getX());
        }
        else
        {
            tempmax.setX(second.getX());
            tempmin.setX(first.getX());
        }

        if (first.getY() >= second.getY())
        {
            tempmax.setY(first.getY());
            tempmin.setY(second.getY());
        }
        else
        {
            tempmax.setY(second.getY());
            tempmin.setY(first.getY());
        }

        if (first.getZ() >= second.getZ())
        {
            tempmax.setZ(first.getZ());
            tempmin.setZ(second.getZ());
        }
        else
        {
            tempmax.setZ(second.getZ());
            tempmin.setZ(first.getZ());
        }

        tempmin.setWorld(first.getWorld());
        tempmax.setWorld(first.getWorld());
    }


    /**
     * Get string of area for save it in a config for example
     * @return String to save
     */
    public String toString() {
        return ConfigUtils.locationToString(min) + ":" + ConfigUtils.locationToString(max);
    }

    /**
     * Get a copy of Area
     * @return copied Area
     */
    public Area copy() {
        return new Area(min, max);
    }

    /**
     * Get minimum corner of area
     * @return minimum corner
     */
    public Location getMin() {
        return this.min;
    }

    /**
     * Get maximum corner of area
     * @return maximum corner
     */
    public Location getMax() {
        return this.max;
    }

    /**
     * Know if location is in this area
     * @param loc Location of thing that you want to check if its in.
     * @return true if is in
     */
    public boolean isInArea(Location loc)
    {
        if (loc == null)
            return false;
        else if (loc.getX() > this.max.getX() || this.min.getX() > loc.getX())
            return false;
        else if (loc.getY() > this.max.getY() || this.min.getY() > loc.getY())
            return false;
        else if (loc.getZ() > this.max.getZ() || this.min.getZ() > loc.getZ())
            return false;

        return true;
    }

    /**
     * Know if location is in this area within a certain range of border
     * @param loc Location of thing that you want to check if its in.
     * @param range range from border
     * @return true if is in
     */
    public boolean isInLimit(Location loc, int range)
    {
        if (loc == null)
            return false;
        else if (loc.getX() > max.getX() - range || min.getX() + range > loc.getX())
            return true;
        else if (loc.getZ() > max.getZ() - range || min.getZ() + range > loc.getZ())
            return true;

        return false;
    }

    /**
     * Get size of area in X
     * @return size
     */
    public int getSizeX() { return max.getBlockX() - min.getBlockX(); }

    /**
     * Get size of area in Y
     * @return size
     */
    public int getSizeY() { return max.getBlockY() - min.getBlockY(); }

    /**
     * Get siez of area in Z
     * @return size
     */
    public int getSizeZ() { return max.getBlockZ() - min.getBlockZ(); }
}
