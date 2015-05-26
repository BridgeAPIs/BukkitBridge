package net.bridgesapi.utils;

import org.bukkit.Location;

public class Selection
{
    private Location firstPoint;
    private Location lastPoint;

    /**
     * Make a selection with two locations if you already know them.
     * @param firstPoint
     * @param lastPoint
     */
    public Selection(Location firstPoint, Location lastPoint)  {
        this.firstPoint = firstPoint;
        this.lastPoint = lastPoint;
    }

    /**
     * Make a selection with nulled location if you will set them after
     */
    public Selection() {
        this(null, null);
    }

    /**
     * Set first location (no need to be the bottom corner)
     * @param firstPoint
     */
    public void setFirstPoint(Location firstPoint) {
        this.firstPoint = firstPoint;
    }

    /**
     * Set the second location (no need to be the upper corner)
     * @param lastPoint
     */
    public void setLastPoint(Location lastPoint)  {
        this.lastPoint = lastPoint;
    }

    /**
     * Get location of minimum point of this selection
     * @return Location of minimum point
     */
    public Location getMinimumPoint() {
        double x = Math.min(this.firstPoint.getX(), this.lastPoint.getBlockX());
        double y = Math.min(this.firstPoint.getY(), this.lastPoint.getBlockY());
        double z = Math.min(this.firstPoint.getZ(), this.lastPoint.getBlockZ());

        return new Location(this.firstPoint.getWorld(), x, y, z);
    }

    /**
     * Get location of maximum point of this selection
     * @return Location of maximum point
     */
    public Location getMaximumPoint() {
        double x = Math.max(this.firstPoint.getX(), this.lastPoint.getBlockX());
        double y = Math.max(this.firstPoint.getY(), this.lastPoint.getBlockY());
        double z = Math.max(this.firstPoint.getZ(), this.lastPoint.getBlockZ());

        return new Location(this.firstPoint.getWorld(), x, y, z);
    }

    /**
     * Build an Area from selection
     * @return Area
     */
    public Area buildArea() {
        return new Area(firstPoint, lastPoint);
    }
}
